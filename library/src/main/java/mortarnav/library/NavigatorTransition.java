package mortarnav.library;

import android.view.View;

import java.util.HashSet;
import java.util.Set;

import mortarnav.library.transition.ScreenTransition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorTransition {

    private Class<? extends View> target;
    private Set<Class<? extends View>> from;
    private boolean fromAny;
    private ScreenTransition transition;

    public static TargetBuilder forView(Class<? extends View> view) {
        return new TargetBuilder(view);
    }

    public static TargetsBuilder forViews(Class<? extends View>... views) {
        return new TargetsBuilder(views);
    }

    public static NavigatorTransition defaultTransition(ScreenTransition transition) {
        NavigatorTransition navigatorTransition = new NavigatorTransition();
        navigatorTransition.transition = transition;
        navigatorTransition.fromAny = true;
        return navigatorTransition;
    }

    public Class<? extends View> getTarget() {
        return target;
    }

    public Set<Class<? extends View>> getFrom() {
        return from;
    }

    public boolean isFromAny() {
        return fromAny;
    }

    public ScreenTransition getTransition() {
        return transition;
    }

    public static class TargetBuilder {

        private NavigatorTransition transition;

        private TargetBuilder(Class<? extends View> target) {
            Preconditions.checkNotNull(target, "Target cannot be null");
            transition = new NavigatorTransition();
            transition.target = target;
        }

        public TargetBuilder from(Class<? extends View> from) {
            Preconditions.checkArgument(!transition.fromAny, "Cannot call from() and fromAny() at the same time");
            Preconditions.checkNotNull(from, "From cannot be null");

            if (transition.from == null) {
                transition.from = new HashSet<>();
            }

            transition.from.add(from);
            return this;
        }

        public TargetBuilder fromAny() {
            Preconditions.checkNull(transition.from, "Cannot call from() and fromAny() at the same time");
            transition.fromAny = true;
            return this;
        }

        public NavigatorTransition withTransition(ScreenTransition transition) {
            this.transition.transition = transition;
            return build();
        }

        public NavigatorTransition withoutTransition() {
            return build();
        }

        private NavigatorTransition build() {
            Preconditions.checkArgument(transition.from != null || transition.fromAny, "Must call from() or fromAny()");
            return this.transition;
        }
    }

    public static class TargetsBuilder {

        private Set<TargetBuilder> builders;

        public TargetsBuilder(Class<? extends View>... targets) {
            Preconditions.checkArgument(targets != null && targets.length > 0, "Targets cannot be null or empty");

            builders = new HashSet<>(targets.length);
            for (Class<? extends View> target : targets) {
                TargetBuilder builder = new TargetBuilder(target);
                builder.fromAny();
                builders.add(builder);
            }
        }

        public Set<NavigatorTransition> withTransition(ScreenTransition transition) {
            Set<NavigatorTransition> transitions = new HashSet<>(builders.size());
            for (TargetBuilder builder : builders) {
                transitions.add(builder.withTransition(transition));
            }

            return transitions;
        }

        public Set<NavigatorTransition> withoutTransition() {
            Set<NavigatorTransition> transitions = new HashSet<>(builders.size());
            for (TargetBuilder builder : builders) {
                transitions.add(builder.withoutTransition());
            }

            return transitions;
        }


    }

}
