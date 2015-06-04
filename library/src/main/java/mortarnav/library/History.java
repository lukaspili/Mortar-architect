package mortarnav.library;

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

    private History(Deque<Entry> entries) {
        this.entries = entries;
    }

    public void push(Screen screen) {
        entries.add(new Entry(screen));
    }

    public boolean canPop() {
        return entries.size() > 1;
    }

    public Screen pop() {
        return entries.pop().screen;
    }

    public Screen peek() {
        return entries.peek().screen;
    }

    public static class Entry {
        private final ViewState state;
        private final Screen screen;

        public Entry(Screen screen) {
            this.state = new ViewState();
            this.screen = screen;
        }
    }
}
