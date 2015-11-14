package architect;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Transitions {

    SimpleArrayMap<String, ViewTransition> transitions = new SimpleArrayMap<>();
    ViewTransition pushDefaultViewTransition;
    ViewTransition showDefaultViewTransition;

    public Transitions setPushDefault(ViewTransition transition) {
        pushDefaultViewTransition = transition;
        return this;
    }

    public Transitions showPushDefault(ViewTransition transition) {
        showDefaultViewTransition = transition;
        return this;
    }

    public Transitions add(String name, ViewTransition transition) {
        transitions.put(name, transition);
        return this;
    }

    ViewTransition find(String name, boolean push) {
        if (name == null) {
            return push ? pushDefaultViewTransition : showDefaultViewTransition;
        }

        return transitions.get(name);
    }
}
