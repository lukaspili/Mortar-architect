package architect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Transitions {

    Map<String, ViewTransition> transitions = new HashMap<>();
    ViewTransition defaultViewTransition;

    public Transitions setDefault(ViewTransition transition) {
        defaultViewTransition = transition;
        return this;
    }

    public Transitions add(String name, ViewTransition transition) {
        transitions.put(name, transition);
        return this;
    }

    ViewTransition find(String name) {
        if (name == null) {
            return defaultViewTransition;
        }

        return transitions.get(name);
    }
}
