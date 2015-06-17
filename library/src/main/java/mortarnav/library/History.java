package mortarnav.library;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

/**
 * An history of scopes...
 * History restoration from instance state bundle is only used during app process kill
 * During simple configuration change, the navigator scope is preserved, and thus the history also
 * See NavigatorLifecycleDelegate.onCreate() to see implementation details
 * <p/>
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

    static History fromBundle(Bundle bundle) {
        ArrayList<Bundle> entryBundles = bundle.getParcelableArrayList(ENTRIES_KEY);
        Deque<Entry> entries = new ArrayDeque<>(entryBundles.size());
        for (Bundle entryBundle : entryBundles) {
            entries.add(Entry.fromBundle(entryBundle));
        }

        ScopeNamer scopeNamer = bundle.getParcelable(SCOPES_NAMER_KEY);

        return new History(entries, scopeNamer);
    }

    static History create(NavigationPath path) {
        History history = new History();
        history.push(path);
        return history;
    }

    Bundle toBundle() {
        Bundle historyBundle = new Bundle();
        ArrayList<Bundle> entryBundles = new ArrayList<>(entries.size());
        for (Entry entry : entries) {
            Bundle entryBundle = entry.toBundle();
            if (entryBundle != null) {
                entryBundles.add(entryBundle);
            }
        }
        historyBundle.putParcelableArrayList(ENTRIES_KEY, entryBundles);
        historyBundle.putParcelable(SCOPES_NAMER_KEY, scopeNamer);

        return historyBundle;
    }

    private final Deque<Entry> entries;
    private final ScopeNamer scopeNamer;

    History() {
        this(new ArrayDeque<Entry>(), new ScopeNamer());
    }

    private History(Deque<Entry> entries, ScopeNamer scopeNamer) {
        this.entries = entries;
        this.scopeNamer = scopeNamer;
    }


    boolean isEmpty() {
        return entries.isEmpty();
    }

    /**
     * Filter should return true to stop the loop
     */
    void filterFromTop(Filter filter) {
        for (Entry entry : entries) {
            if (filter.filter(entry)) {
                return;
            }
        }
    }

    void removeAll(Collection<Entry> entries) {
        this.entries.removeAll(entries);
    }

    void copy(History history) {
        Preconditions.checkArgument(entries.isEmpty(), "Cannot copy new history if previous one is not empty");
        entries.addAll(history.entries);

        Logger.d("New history");
        for (Entry entry : entries) {
            Logger.d("Entry scope = %s", entry.scopeName);
        }
    }

    History.Entry find(String scope) {
        for (History.Entry entry : entries) {
            if (entry.scopeName.equals(scope)) {
                return entry;
            }
        }

        return null;
    }

    void push(NavigationPath navigationPath) {
        NavigationScope scope = navigationPath.withScope();

        Entry entry = new Entry(scopeNamer.getName(scope), scope, navigationPath);
        Preconditions.checkArgument(!entries.contains(entry), "An entry with the same navigation path is already present in history");
        entries.addFirst(entry);
    }

    /**
     * At least 2 entries
     */
    boolean canKill() {
        return entries.size() > 1;
    }

    void killTop() {
        entries.getFirst().dead = true;
    }

    /**
     * Return the most top entry that is alive
     */
    History.Entry peekAlive() {
        for (Entry entry : entries) {
            if (!entry.dead) {
                return entry;
            }
        }

        return null;
    }

    static class Entry {
        final String scopeName;
        final NavigationPath path;
        final NavigationScope scope;
        SparseArray<Parcelable> state;
        boolean dead;

        public Entry(String scopeName, NavigationScope scope, NavigationPath path) {
            Preconditions.checkArgument(scopeName != null && !scopeName.isEmpty(), "Scope name cannot be null nor empty");
            Preconditions.checkNotNull(scope, "Scope cannot be null");
            Preconditions.checkNotNull(path, "Path cannot be null");
            this.scopeName = scopeName;
            this.scope = scope;
            this.path = path;
        }

        private Bundle toBundle() {
            if (dead) {
                // don't save dead entry
                // its scope will be destroyed anyway
                return null;
            }

            Bundle bundle = new Bundle();
            bundle.putString(SCOPE_KEY, scopeName);
            bundle.putParcelable(PATH_KEY, path);
            bundle.putSparseParcelableArray(STATE_KEY, state);
            return bundle;
        }

        private static Entry fromBundle(Bundle bundle) {
            NavigationPath path = bundle.getParcelable(PATH_KEY);

            // new entry with new scope instance, but preserving previous scope name
            Entry entry = new Entry(bundle.getString(SCOPE_KEY), path.withScope(), path);
            entry.state = bundle.getSparseParcelableArray(STATE_KEY);
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

    interface Filter {
        boolean filter(Entry entry);
    }
}
