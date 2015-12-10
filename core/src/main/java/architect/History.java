package architect;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.util.SimpleArrayMap;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import architect.hook.Hook;
import architect.behavior.ReceivesResult;
import architect.service.Service;

/**
 * Architect history
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class History {

    private static final String ENTRIES_KEY = "ENTRIES";
    private static final String SCREEN_KEY = "SCREEN";
    private static final String STATE_KEY = "VIEW_STATE";
    private static final String SERVICE_KEY = "SERVICE";
    private static final String TAG_KEY = "TAG";
    private static final String EXTRAS_KEY = "EXTRAS";

    private final ScreenParceler parceler;
    private final Hooks hooks;
    private List<Entry> entries = new ArrayList<>();

    History(ScreenParceler parceler, Hooks hooks) {
        this.parceler = parceler;
        this.hooks = hooks;
    }

    void populateFromBundle(Bundle bundle) {
        ArrayList<Bundle> entryBundles = bundle.getParcelableArrayList(ENTRIES_KEY);
        if (entryBundles != null) {
            entries = new ArrayList<>(entryBundles.size());
            if (!entryBundles.isEmpty()) {
                Entry entry;
                for (int i = 0; i < entryBundles.size(); i++) {
                    entry = Entry.fromBundle(entryBundles.get(i), parceler);
                    populate(entry);
                }
            }
        }
    }

    void populateFromStack(Stack stack) {
        Entry entry;
        for (int i = 0; i < stack.getEntries().size(); i++) {
            entry = stack.getEntries().get(i);
            populate(entry);
        }
    }

    Bundle toBundle() {
        Bundle historyBundle = new Bundle();
        ArrayList<Bundle> entryBundles = new ArrayList<>(entries.size());
        for (Entry entry : entries) {
            Bundle entryBundle = entry.toBundle(parceler);
            if (entryBundle != null) {
                entryBundles.add(entryBundle);
            }
        }
        historyBundle.putParcelableArrayList(ENTRIES_KEY, entryBundles);

        return historyBundle;
    }

//    boolean isEmpty() {
//        return entries.isEmpty();
//    }
//
//    boolean canReplace() {
//        return entries.size() > 0;
//    }
//

    /**
     * At least 1 entry
     */
    boolean canKill(String service, Validator<List<Entry>> validator) {
        return validator.isValid(entries(service));
    }

    /**
     * Create new entry in history
     */
    Entry add(Screen screen, String service, String tag, Bundle extras) {
        final Entry entry = new Entry(screen, service, tag, extras);
        entries.add(entry);

        hooks.hookHistory(new Hooks.HookOn<Hook.HistoryHook>() {
            @Override
            public void hook(Hook.HistoryHook on) {
                on.onAddEntry(entry);
            }
        });

        return entry;
    }

    /**
     * Kill the top entry
     *
     * @param forReplace if the entry is killed during a replace
     * @return the top killed entry
     */
    Entry kill(String service, Object result, final boolean forReplace) {
        Entry killed = null;
        for (int i = entries.size() - 1; i >= 0; i--) {
            if (entries.get(i).service.equals(service)) {
                killed = entries.remove(i);
                break;
            }
        }

        Preconditions.checkNotNull(killed, "No entry to kill");

        if (!entries.isEmpty()) {
            setResult(result);
        }

        final Entry finalKilled = killed;
        hooks.hookHistory(new Hooks.HookOn<Hook.HistoryHook>() {
            @Override
            public void hook(Hook.HistoryHook on) {
                if (forReplace) {
                    on.onReplaceEntry(finalKilled);
                } else {
                    on.onKillEntry(finalKilled);
                }
            }
        });

        return killed;
    }

    /**
     * Kill all but root
     *
     * @return the killed entries, in the historical order
     */
    List<Entry> killAllButRoot(Object result) {
        // nothing to kill
        if (entries.size() == 1) {
            return new ArrayList<>(0);
        }

        // all entries but root, in reverse order
        final List<Entry> killed = new ArrayList<>(entries.size() - 1);
        for (int i = entries.size() - 1; i >= 1; i--) {
            killed.add(entries.remove(i));
        }

        setResult(result);

        hooks.hookHistory(new Hooks.HookOn<Hook.HistoryHook>() {
            @Override
            public void hook(Hook.HistoryHook on) {
                on.onKillEntries(killed);
            }
        });

        return killed;
    }

    List<Entry> killUntil(String service, int index) {
        final List<Entry> serviceEntries = entries(service);
        final List<Entry> killed = new ArrayList<>(serviceEntries.size() - index);
        Entry entry;
        for (int i = serviceEntries.size() - 1; i >= 0; i--) {
            if (i == index) {
                break;
            }

            entry = serviceEntries.remove(i);
            entries.remove(entry);
            killed.add(entry);
        }

        hooks.hookHistory(new Hooks.HookOn<Hook.HistoryHook>() {
            @Override
            public void hook(Hook.HistoryHook on) {
                on.onKillEntries(killed);
            }
        });

        return killed;
    }

    /**
     * Kill all
     *
     * @return the killed entries, in the historical order
     */
    List<Entry> killAll(String service) {
        final List<Entry> killed = new ArrayList<>();
        for (int i = entries.size() - 1; i >= 0; i--) {
            if (entries.get(i).service.equals(service)) {
                killed.add(entries.remove(i));
            }
        }

        hooks.hookHistory(new Hooks.HookOn<Hook.HistoryHook>() {
            @Override
            public void hook(Hook.HistoryHook on) {
                on.onKillEntries(killed);
            }
        });

        return killed;
    }

    List<Entry> entries() {
        List<Entry> list = new ArrayList<>(entries.size());
        for (int i = 0; i < entries.size(); i++) {
            list.add(entries.get(i));
        }

        return list;
    }

    List<Entry> entries(String service) {
        List<Entry> list = new ArrayList<>();
        Entry entry;
        for (int i = 0; i < entries.size(); i++) {
            entry = entries.get(i);
            if (entry.service.equals(service)) {
                list.add(entry);
            }
        }

        return list;
    }

//    List<Entry> entriesDescending() {
//        List<Entry> list = new ArrayList<>(entries.size());
//        for (int i = entries.size() - 1; i >= 0; i--) {
//            list.add(entries.get(i));
//        }
//
//        return list;
//    }

    /**
     * Set the result, or null if no result (result = null)
     */
    private void setResult(Object result) {
        Entry entry = entries.get(entries.size() - 1);
        if (entry.screen instanceof ReceivesResult) {
            //noinspection unchecked
            ((ReceivesResult) entry.screen).setResult(result);
        }
    }

    private List<Entry> filterByDescending(String service, boolean killAll) {
        List<Entry> filtered = new ArrayList<>();
        Entry entry;
        for (int i = entries.size() - 1; i >= 0; i--) {
            entry = entries.get(i);
            if (entry.service.equals(service)) {
                if (killAll) {
                    entries.remove(i);
                }
                filtered.add(entry);
            }
        }

        return filtered;
    }

//    Entry getLastAlive() {
//        if (entries.isEmpty()) {
//            return null;
//        }
//
//        Entry entry;
//        for (int i = entries.size() - 1; i >= 0; i--) {
//            entry = entries.get(i);
//            if (!entry.dead) {
//                return entry;
//            }
//        }
//
//        return null;
//    }


//    List<Entry> removeAllDead() {
//        if (entries.isEmpty()) {
//            return null;
//        }
//
//        List<Entry> dead = new ArrayList<>(entries.size());
//        Entry entry;
//        for (int i = 0; i < entries.size(); i++) {
//            entry = entries.get(i);
//            if (entry.dead) {
//                dead.add(entry);
//                entries.remove(i);
//            }
//        }
//
//        return dead;
//    }

//    Entry getLast() {
//        Preconditions.checkArgument(!entries.isEmpty(), "Cannot get last entry on empty history");
//        return entries.get(entries.size() - 1);
//    }
//
//    Entry getLeftOf(Entry entry) {
//        int index = entries.indexOf(entry);
//        Preconditions.checkArgument(index >= 0, "Get left of an entry that does not exist in history");
////        if (index == 0) {
////            Entry previous = previousRoot;
////            previousRoot = null;
////            return previous;
////        }
//
//        return entries.get(index - 1);
//    }

    Entry getTopDispatched() {
        if (!entries.isEmpty()) {
            Entry entry;
            for (int i = entries.size() - 1; i >= 0; i--) {
                entry = entries.get(i);
                if (entry.dispatched) {
                    return entry;
                }
            }
        }

        return null;
    }

//    Entry getTop() {
//        if (!entries.isEmpty()) {
//            Entry entry;
//            for (int i = entries.size() - 1; i >= 0; i--) {
//                entry = entries.get(i);
//                if (entry.dispatched) {
//                    return entry;
//                }
//            }
//        }
//
//        return null;
//    }

    boolean existInHistory(Entry entry) {
        return entries.contains(entry);
    }

//    int getEntryScopeId(Entry entry) {
//        return tracker.get(entry);
//    }

//    List<Entry> getLastWithModals() {
//        Preconditions.checkArgument(entries.size() > 1, "At least 2 entries (non-modal + n modals)");
//        List<Entry> previous = new ArrayList<>(entries.size() - 2);
//        Entry e;
//        for (int i = entries.size() - 1; i >= 0; i--) {
//            e = entries.get(i);
//            previous.add(e);
//
//            if (!e.isModal()) {
//                // when we encounter non-modal, return the previous stack
//                // with the first non-modal
//                return previous;
//            }
//        }
//
//        throw new IllegalStateException("Invalid reach");
//    }

    private void populate(final Entry entry) {
        entry.dispatched = true;
        entries.add(entry);

        hooks.hookHistory(new Hooks.HookOn<Hook.HistoryHook>() {
            @Override
            public void hook(Hook.HistoryHook on) {
                on.onAddEntry(entry);
            }
        });
    }

    public static class Entry {
        public final String service;
        public final String tag;
        public final Screen screen;
        public final Bundle extras;
        public SparseArray<Parcelable> viewState;

        //todo: Publoic ?
        public boolean dispatched;

        public Entry(Screen screen, String service, String tag, Bundle extras) {
            Preconditions.checkNotNull(screen, "Screen cannot be null");
            Preconditions.checkNotNull(service, "Service cannot be null");
            this.screen = screen;
            this.service = service;
            this.extras = extras;
            this.tag = tag;
        }

        private Bundle toBundle(ScreenParceler parceler) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(SCREEN_KEY, parceler.wrap(screen));
            bundle.putSparseParcelableArray(STATE_KEY, viewState);
            bundle.putString(SERVICE_KEY, service);
            bundle.putString(TAG_KEY, tag);
            bundle.putBundle(EXTRAS_KEY, extras);
            return bundle;
        }

        private static Entry fromBundle(Bundle bundle, ScreenParceler parceler) {
            Entry entry = new Entry(parceler.unwrap(bundle.getParcelable(SCREEN_KEY)), bundle.getString(SERVICE_KEY), bundle.getString(TAG_KEY), bundle.getBundle(EXTRAS_KEY));
            entry.viewState = bundle.getSparseParcelableArray(STATE_KEY);
            return entry;
        }

        @Override
        public String toString() {
            return screen.toString();
        }
    }

//    private interface Filter {
//        boolean filter(Entry entry);
//    }


}
