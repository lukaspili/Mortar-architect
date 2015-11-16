package architect.service.presentation;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Transitions {

    public static final String NO_TRANSITION = "";

    private SimpleArrayMap<String, Transition> transitions = new SimpleArrayMap<>();
    private Transition defaultTransition;

    public Transitions setDefault(Transition transition) {
        defaultTransition = transition;
        return this;
    }

    public Transitions add(String name, Transition transition) {
        Preconditions.checkArgument(name != null && name.length() > 0, "Transition name may not be blank");
        transitions.put(name, transition);
        return this;
    }

    Transition find(String name) {
        if (name == null) {
            return defaultTransition;
        }

        return transitions.get(name);
    }
}
