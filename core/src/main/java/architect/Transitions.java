package architect;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Transitions {

    SimpleArrayMap<String, Transition> transitions = new SimpleArrayMap<>();
    Transition pushDefaultViewTransition;
    Transition showDefaultViewTransition;

    public Transitions setPushDefault(Transition transition) {
        pushDefaultViewTransition = transition;
        return this;
    }

    public Transitions showPushDefault(Transition transition) {
        showDefaultViewTransition = transition;
        return this;
    }

    public Transitions add(String name, Transition transition) {
        transitions.put(name, transition);
        return this;
    }

    Transition find(String name, boolean push) {
        if (name == null) {
            return push ? pushDefaultViewTransition : showDefaultViewTransition;
        }

        return transitions.get(name);
    }
}
