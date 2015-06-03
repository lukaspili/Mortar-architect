package mortarnav.library;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class History {

    public static History single(Screen screen) {
        Deque<Screen> history = new ArrayDeque<>();
        history.add(screen);
        return new History(history);
    }

    private final Deque<Screen> states;

    private History(Deque<Screen> states) {
        this.states = states;
    }
}
