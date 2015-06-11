package mortarnav.library;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import mortarnav.library.screen.Screen;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class History {

    public static History create(Screen... screens) {
        Preconditions.checkNotNull(screens, "Screens null");
        Preconditions.checkArgument(screens.length > 0, "Cannot create empty history");

        Deque<Entry> entries = new ArrayDeque<>();
        for (Screen screen : screens) {
            entries.add(new Entry(screen));
        }
        return new History(entries);
    }

    public static History fromBundle(Bundle bundle) {
        ArrayList<Bundle> entryBundles = bundle.getParcelableArrayList("ENTRIES");
        Deque<Entry> entries = new ArrayDeque<>(entryBundles.size());
        for (Bundle entryBundle : entryBundles) {
            Screen screen = (Screen) entryBundle.get("SCREEN");
            Entry entry = new Entry(screen);
            entry.state = entryBundle.getSparseParcelableArray("VIEW_STATE");
            entries.add(entry);
        }
        return new History(entries);
    }

    public Bundle toBundle() {
        Bundle historyBundle = new Bundle();
        ArrayList<Bundle> entryBundles = new ArrayList<>(entries.size());
        for (Entry entry : entries) {
            entryBundles.add(entry.toBundle());
        }
        historyBundle.putParcelableArrayList("ENTRIES", entryBundles);
        return historyBundle;
    }

    private final Deque<Entry> entries;

    History() {
        this(new ArrayDeque<Entry>());
    }

    private History(Deque<Entry> entries) {
        this.entries = entries;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public void replaceBy(History history) {
        entries.clear();
        entries.addAll(history.entries);
    }

    public History.Entry findScreen(String scopeName) {
        for (History.Entry entry : entries) {
            if (entry.getScreen().getMortarScopeName().equals(scopeName)) {
                return entry;
            }
        }

        return null;
    }

    public void push(Screen screen) {
        entries.addFirst(new Entry(screen));
    }

    /**
     * At least 2 entries
     */
    public boolean canPop() {
        return entries.size() > 1;
    }

    public void pop() {
        entries.removeFirst();
    }

    public History.Entry peek() {
        return entries.peekFirst();
    }

    public static class Entry {
        private final Screen screen;
        private SparseArray<Parcelable> state;

        public Entry(Screen screen) {
            Preconditions.checkNotNull(screen, "Screen cannot be null");
            Preconditions.checkNotNull(screen.getMortarScopeName(), "Screen scope name cannot be null");
            this.screen = screen;
        }

        Bundle toBundle() {
            Bundle bundle = new Bundle();
            bundle.putSerializable("SCREEN", screen);
            bundle.putSparseParcelableArray("VIEW_STATE", state);
            return bundle;
        }

        public Screen getScreen() {
            return screen;
        }

        public SparseArray<Parcelable> getState() {
            return state;
        }

        public void setState(SparseArray<Parcelable> state) {
            this.state = state;
        }
    }
}
