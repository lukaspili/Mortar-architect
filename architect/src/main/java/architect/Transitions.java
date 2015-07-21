package architect;

import android.support.v4.util.ArrayMap;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Transitions {

    private static final int MATCH_NONE = 0;
    private static final int MATCH_EXACT = 1;
    private static final int MATCH_WILDCARD = 2;

    /**
     * Mapping of target -> from -> transition
     * This structure should be optimized to something better for android memory
     */
    private final Map<Key, Map<Key, ViewTransition>> transitions;

    public Transitions() {
        transitions = new HashMap<>();
    }

    public Transitions register(TransitionsMapping mapping) {
        Preconditions.checkNotNull(mapping, "Mapping cannot be null");

        if (!mapping.list.isEmpty()) {
            for (TransitionsMapping.Mapping map : mapping.list) {
                register(map);
            }
        }

        return this;
    }

    private Transitions register(TransitionsMapping.Mapping mapping) {
        Key target = new Key(mapping.view);
        Preconditions.checkArgument(!transitions.containsKey(target), "Cannot register the same transition destination view multiple times: %s", mapping.view);

        Map<Key, ViewTransition> targetTransitions;
        if (mapping.from == null || mapping.from.length == 0) {
            // from any
            targetTransitions = new HashMap<>(1);
            targetTransitions.put(new Key(), mapping.transition);
        } else {
            targetTransitions = new HashMap<>(mapping.from.length);
            for (Class cls : mapping.from) {
                targetTransitions.put(new Key(cls), mapping.transition);
            }
        }

        transitions.put(target, targetTransitions);

        return this;
    }

    ViewTransition findTransition(View originView, View destinationView, ViewTransitionDirection direction) {
        // depending on transition direction, the target view is either the origin or destination
        View target = direction == ViewTransitionDirection.FORWARD ? destinationView : originView;
        View from = direction == ViewTransitionDirection.FORWARD ? originView : destinationView;
        return findTransition(target, from);
    }

    private ViewTransition findTransition(View targetView, View fromView) {
        Key targetKey = getBestMatchKey(targetView.getClass(), transitions.keySet());
        if (targetKey == null) {
            return null;
        }

        Map<Key, ViewTransition> targetTransitions = transitions.get(targetKey);
        Key fromKey = getBestMatchKey(fromView.getClass(), targetTransitions.keySet());
        if (fromKey == null) {
            return null;
        }

        ViewTransition transition = targetTransitions.get(fromKey);
        return transition;
    }

    /**
     * Get best key that matches in the order: Exact, Wildcard, None
     */
    private Key getBestMatchKey(Class cls, Set<Key> keys) {
        int match = compare(cls, keys);
        if (match == MATCH_NONE) {
            return null;
        }

        return match == MATCH_EXACT ? new Key(cls) : new Key();
    }

    private int compare(Class cls, Set<Key> keys) {
        int bestMatch = MATCH_NONE;
        for (Key key : keys) {
            if (key.cls != null && key.cls.equals(cls)) {
                return MATCH_EXACT;
            }

            if (key.cls == null && bestMatch == MATCH_NONE) {
                bestMatch = MATCH_WILDCARD;
            }
        }

        return bestMatch;
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
    }
}
