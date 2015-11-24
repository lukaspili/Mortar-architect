package architect;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import architect.behavior.ReceivesResult;

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
    private final HistoryTracker tracker = new HistoryTracker();
    private List<Entry> entries = new ArrayList<>();

    History(ScreenParceler parceler) {
        this.parceler = parceler;
    }

    void populateFromBundle(Bundle bundle) {
        ArrayList<Bundle> entryBundles = bundle.getParcelableArrayList(ENTRIES_KEY);
        if (entryBundles != null) {
            entries = new ArrayList<>(entryBundles.size());
            if (!entryBundles.isEmpty()) {
                Entry entry;
                for (int i = 0; i < entryBundles.size(); i++) {
                    entry = Entry.fromBundle(entryBundles.get(i), parceler);
                    entry.dispatched = true;
                    tracker.increment(entry);

                    entries.add(entry);
                }
            }
        }
    }

    void populateFromStack(Stack stack) {
        Entry entry;
        for (int i = 0; i < stack.getEntries().size(); i++) {
            entry = stack.getEntries().get(i);
            entry.dispatched = true;
            tracker.increment(entry);

            entries.add(entry);
        }
    }

//    void init(Screen... paths) {
//        entries = new ArrayList<>();
//
//        Entry entry;
//        for (int i = 0; i < paths.length; i++) {
//            entry = add(paths[i], NAV_TYPE_PUSH, null, null);
//            entry.dispatched = true;
//        }
//    }

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

    boolean isEmpty() {
        return entries.isEmpty();
    }

    boolean canReplace() {
        return entries.size() > 0;
    }

    /**
     * At least 2 alive entries
     */
    boolean canKill() {
        return entries.size() > 1;
    }

    /**
     * Create new entry in history
     */
    Entry add(Screen screen, String service, String tag, Bundle extras) {
        Entry entry = new Entry(screen, service, tag, extras);
        entries.add(entry);
        tracker.increment(entry);

        Logger.d("Add entry: %s", entry.screen);

        return entry;
    }

    /**
     * Kill the latest alive entry
     *
     * @return the killed entry
     */
    Entry kill(Object result, boolean forReplace) {
        Entry killed = entries.remove(entries.size() - 1);

        // when replacing, we won't decrement the scope of the exit entry because
        // it can create conflict between enter and exit scope names during dispatch
        if (!forReplace) {
            tracker.decrement(killed);
        }

        if (!entries.isEmpty()) {
            setResult(result);
        }

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

        List<Entry> killed = new ArrayList<>(entries.size() - 1);
        Entry entry;
        for (int i = entries.size() - 1; i >= 1; i--) {
            entry = entries.remove(i);
            tracker.decrement(entry);
            killed.add(entry);
        }

        setResult(result);

        return killed;
    }

    /**
     * Kill all
     *
     * @return the killed entries, in the historical order
     */
    List<Entry> killAll() {
        List<Entry> killed = new ArrayList<>(entries.size());
        Entry entry;
        for (int i = entries.size() - 1; i >= 0; i--) {
            entry = entries.remove(i);
            tracker.decrement(entry);
            killed.add(entry);
        }

        return killed;
    }

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

    List<Entry> getEntries() {
        return entries;
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

    int getEntryScopeId(Entry entry) {
        return tracker.get(entry);
    }

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

    public static class Entry {
        final String service;
        final String tag;
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
}
