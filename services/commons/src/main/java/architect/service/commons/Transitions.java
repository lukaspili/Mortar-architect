package architect.service.commons;

import android.support.v4.util.SimpleArrayMap;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Transitions<T> {

    public static final String NO_TRANSITION = "";

    private SimpleArrayMap<String, T> transitions = new SimpleArrayMap<>();
    private T defaultTransition;

    public Transitions setDefault(T transition) {
        defaultTransition = transition;
        return this;
    }

    public Transitions add(String name, T transition) {
        Preconditions.checkArgument(name != null && name.length() > 0, "Transition name may not be blank");
        transitions.put(name, transition);
        return this;
    }

    public T find(String name) {
        if (name == null) {
            return defaultTransition;
        }

        return transitions.get(name);
    }
}
