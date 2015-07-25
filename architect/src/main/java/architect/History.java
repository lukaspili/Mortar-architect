package architect;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * An history of scopes
 *
 * History restoration from instance state bundle is only used during app process kill
 * During simple configuration change, the navigator scope is preserved, and thus the history also
 * See NavigatorLifecycleDelegate.onCreate() to see implementation details
 *
 * History also persists its ScopeNamer instance, in order to preserve scope names
 * between state restoration (process kill)
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class History {

    private static final String ENTRIES_KEY = "ENTRIES";
    private static final String PATH_KEY = "PATH";
    private static final String SCOPE_KEY = "SCOPE_STATE";
    private static final String STATE_KEY = "VIEW_STATE";
    private static final String SCOPES_NAMER_KEY = "SCOPES_IDS";
    private static final String NAV_TYPE_KEY = "NAV_TYPE";

    static final int NAV_TYPE_PUSH = 1;
    static final int NAV_TYPE_MODAL = 2;

    private final StackableParceler parceler;
    private List<Entry> entries;
    private ScopeNamer scopeNamer;

    History(StackableParceler parceler) {
        this.parceler = parceler;
    }

    void init(Bundle bundle) {
        scopeNamer = bundle.getParcelable(SCOPES_NAMER_KEY);

        ArrayList<Bundle> entryBundles = bundle.getParcelableArrayList(ENTRIES_KEY);
        if (entryBundles != null) {
            entries = new ArrayList<>(entryBundles.size());
            if (!entryBundles.isEmpty()) {
                for (int i = 0; i < entryBundles.size(); i++) {
                    entries.add(Entry.fromBundle(entryBundles.get(i), parceler));
                }
            }
        }
    }

    void init(ScreenPath... paths) {
        entries = new ArrayList<>();
        scopeNamer = new ScopeNamer();

        for (int i = 0; i < paths.length; i++) {
            add(paths[i], NAV_TYPE_PUSH);
        }
    }

    Bundle toBundle() {
        Bundle historyBundle = new Bundle();
        historyBundle.putParcelable(SCOPES_NAMER_KEY, scopeNamer);

        if (parceler != null) {
            ArrayList<Bundle> entryBundles = new ArrayList<>(entries.size());
            for (Entry entry : entries) {
                Bundle entryBundle = entry.toBundle(parceler);
                if (entryBundle != null) {
                    entryBundles.add(entryBundle);
                }
            }
            historyBundle.putParcelableArrayList(ENTRIES_KEY, entryBundles);
        }

        return historyBundle;
    }

    boolean isEmpty() {
        return entries.isEmpty();
    }

    boolean shouldInit() {
        return entries == null;
    }

    Entry add(ScreenPath path, int navType) {
        if (navType == NAV_TYPE_MODAL) {
            // modal
            Preconditions.checkArgument(!entries.isEmpty(), "Cannot add modal on empty history");
        } else {
            // push and history not empty
            if (!entries.isEmpty()) {
                Preconditions.checkArgument(!getLast().isModal(), "Cannot push new path on modal");
            }
        }

        Entry entry = new Entry(scopeNamer.getName(path), path, navType);
        Preconditions.checkArgument(!entries.contains(entry), "An entry with the same navigation path is already present in history");
        entries.add(entry);

        return entry;
    }

    /**
     * At least 2 alive entries
     */
    boolean canKill() {
        if (entries.size() < 2) {
            return false;
        }

        return true;

//        int notdead = 0;
//        for (int i = entries.size() - 1; i >= 0; i--) {
//            if (!entries.get(i).dead) {
//                notdead++;
//            }
//
//            if (notdead > 1) {
//                return true;
//            }
//        }
//
//        return false;
    }

    /**
     * Kill the latest alive entry
     *
     * @return the killed entry
     */
    Entry kill() {
        return entries.remove(entries.size() - 1);
    }

    List<Entry> killAllButRoot() {
        List<Entry> killed = new ArrayList<>(entries.subList(1, entries.size() - 1));
        entries.removeAll(killed);
        return killed;
    }

    /**
     * Kill all, including root or not
     * The returned entries don't include the root entry though
     *
     * @return the killed entries, in the historical order
     */
    List<Entry> killAll() {
        List<Entry> killed = new ArrayList<>(entries);
        entries.clear();
        return killed;

//        List<Entry> killed = new ArrayList<>(rootIncluded ? entries.size() : entries.size() - 1);
//        Entry entry;
//        for (int i = entries.size() - 1; i > (rootIncluded ? -1 : 0); i--) {
//            entry = entries.get(i);
//            entry.dead = true;
//
//            if (i != 0) {
//                // never include root
//                killed.add(entry);
//            }
//        }
//
//        return killed;
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

    void remove(Entry entry) {
        boolean removed = entries.remove(entry);
        Preconditions.checkArgument(removed, "Entry to remove does not exist");
    }

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

    @NonNull
    Entry getLast() {
        Preconditions.checkArgument(!entries.isEmpty(), "Cannot get last entry on empty history");
        return entries.get(entries.size() - 1);
    }

    Entry getLeftOf(Entry entry) {
        int index = entries.indexOf(entry);
        Preconditions.checkArgument(index >= 0, "Get left of an entry that does not exist in history");
        if (index == 0) {
            return null;
        }

        return entries.get(index - 1);
    }

    boolean existInHistory(Entry entry) {
        return entries.contains(entry);
    }

    List<Entry> getPreviousOfModal(Entry entry) {
        int index = entries.indexOf(entry);
        Preconditions.checkArgument(index > 0, "Invalid entry modal index in history");

        List<Entry> previous = new ArrayList<>(entries.size() - index);
        Entry e;
        for (int i = index - 1; i >= 0; i--) {
            e = entries.get(i);
            previous.add(e);
            if (!e.isModal()) {
                // when we encounter non modal, return the previous stack
                return previous;
            }
        }

        throw new IllegalStateException("Invalid reach");
    }

    static class Entry {
        final String scopeName;
        final ScreenPath path;
        final int navType;
        SparseArray<Parcelable> viewState;

//        Object result;
//
        //        boolean dead;
//        Object returnsResult;
//        Object receivedResult;
//        ViewTransitionDirection direction;

        public Entry(String scopeName, ScreenPath path, int navType) {
            Preconditions.checkArgument(scopeName != null && !scopeName.isEmpty(), "Scope name cannot be null nor empty");
            Preconditions.checkNotNull(path, "Path cannot be null");
            Preconditions.checkArgument(navType == NAV_TYPE_PUSH || navType == NAV_TYPE_MODAL, "Nav type invalid");
            this.scopeName = scopeName;
            this.path = path;
            this.navType = navType;
        }

        boolean isModal() {
            return navType == NAV_TYPE_MODAL;
        }

        private Bundle toBundle(StackableParceler parceler) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(PATH_KEY, parceler.wrap(path));
            bundle.putSparseParcelableArray(STATE_KEY, viewState);
            bundle.putInt(NAV_TYPE_KEY, navType);
            return bundle;
        }

        private static Entry fromBundle(Bundle bundle, StackableParceler parceler) {
            ScreenPath path = parceler.unwrap(bundle.getParcelable(PATH_KEY));

            // new entry with new scope instance, but preserving previous scope name
            Entry entry = new Entry(bundle.getString(SCOPE_KEY), path, bundle.getInt(NAV_TYPE_KEY));
            entry.viewState = bundle.getSparseParcelableArray(STATE_KEY);
            return entry;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

            return scopeName.equals(entry.scopeName);

        }

        @Override
        public int hashCode() {
            return scopeName.hashCode();
        }
    }
}
