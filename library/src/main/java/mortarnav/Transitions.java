package mortarnav;

import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import mortarnav.transition.ScreenTransition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Transitions {

    /**
     * Mapping of target -> from -> transition
     */
    private final Map<Key, Map<Key, ScreenTransition>> transitions;

    public Transitions() {
        transitions = new HashMap<>();
    }

    /**
     * Allows to register transitions by injection
     */
    @Inject
    public void setTransitions(List<Transition> transitions) {
        register(transitions);
    }

    public Transitions register(List<Transition> transitions) {
        Preconditions.checkNotNull(transitions, "Transitions cannot be null");

        for (Transition transition : transitions) {
            register(transition);
        }

        return this;
    }

    public Transitions register(Transition transition) {
        Preconditions.checkNotNull(transition, "Transition cannot be null");

        Key target = new Key(transition.getTarget());
        Preconditions.checkArgument(!transitions.containsKey(target), "Cannot register one transition target multiple times");

        Map<Key, ScreenTransition> targetTransitions;
        if (transition.isFromAny()) {
            targetTransitions = new HashMap<>(1);
            targetTransitions.put(new Key(), transition.getTransition());
        } else {
            targetTransitions = new HashMap<>(transition.getFrom().size());
            for (Class cls : transition.getFrom()) {
                targetTransitions.put(new Key(cls), transition.getTransition());
            }
        }

        transitions.put(target, targetTransitions);

        return this;
    }

    ScreenTransition findTransition(View targetView, View fromView) {
        Key targetKey = getBestMatchKey(targetView.getClass(), transitions.keySet());
        if (targetKey == null) {
            return null;
        }

        Map<Key, ScreenTransition> targetTransitions = transitions.get(targetKey);
        Key fromKey = getBestMatchKey(fromView.getClass(), targetTransitions.keySet());
        if (fromKey == null) {
            return null;
        }

        return targetTransitions.get(fromKey);
    }

    /**
     * Get best key that matches in the order: Exact, Wildcard, None
     */
    private Key getBestMatchKey(Class cls, Set<Key> keys) {
        Match match = compare(cls, keys);
        if (match == Match.NONE) {
            return null;
        }

        return match == Match.EXACT ? new Key(cls) : new Key();
    }

    private Match compare(Class cls, Set<Key> keys) {
        Match bestMatch = Match.NONE;
        for (Key key : keys) {
            if (key.cls != null && key.cls.equals(cls)) {
                return Match.EXACT;
            }

            if (key.cls == null && bestMatch == Match.NONE) {
                bestMatch = Match.WILDCARD;
            }
        }

        return bestMatch;
    }

    public enum Match {
        EXACT, WILDCARD, NONE
    }

    public static class Key {
        private final Class cls;

        public Key() {
            this(null);
        }

        private Key(Class cls) {
            this.cls = cls;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            return !(cls != null ? !cls.equals(key.cls) : key.cls != null);
        }

        @Override
        public int hashCode() {
            return cls != null ? cls.hashCode() : 0;
        }

//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            Key key = (Key) o;
//
//            if (cls != null && key.cls != null) {
//                return cls.equals(key.cls);
//            }
//
//            return cls == null && key.cls == null;
//
//        }
    }
}
