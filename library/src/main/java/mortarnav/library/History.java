package mortarnav.library;

import android.os.Parcelable;
import android.util.SparseArray;

import java.util.ArrayDeque;
import java.util.Deque;

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

    private final Deque<Entry> entries;

    History() {
        this(new ArrayDeque<Entry>());
    }

    private History(Deque<Entry> entries) {
        this.entries = entries;
    }

    public void from(History history) {
        entries.clear();
        entries.addAll(history.entries);
    }

//    public Entry next() {
//        Entry next = null;
//        for (Entry entry : entries) {
//            if (entry.isCommitted()) {
//                // exit loop once we hit last to-be committed
//                break;
//            }
//
//            next = entry;
//        }
//
//        return next;
//    }

//    public boolean hasNext() {
//        return next() != null;
//    }

//    public Entry current() {
//        for (Entry entry : entries) {
//            if (entry.isCommitted()) {
//                // return last committed
//                return entry;
//            }
//        }
//
//        return null;
//    }

    public History.Entry findScreen(String scopeName) {
        for (History.Entry entry : entries) {
            if (entry.getScreen().getScopeName().equals(scopeName)) {
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
            Preconditions.checkNotNull(screen.getScopeName(), "Screen scope name cannot be null");
            this.screen = screen;
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
