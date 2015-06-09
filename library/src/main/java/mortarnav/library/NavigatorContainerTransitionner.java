package mortarnav.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.View;

import mortarnav.library.transition.ScreenTransition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorContainerTransitionner {

    private static final int DURATION = 400;

    private final NavigatorTransitions transitions;

    public NavigatorContainerTransitionner(NavigatorTransitions transitions) {
        this.transitions = transitions;
    }

    public void transition(View originView, View destinationView, Dispatcher.Direction direction, final Dispatcher.TraversalCallback callback) {
        // depending on transition direction, the target view is either the origin or destination
        View target = direction == Dispatcher.Direction.FORWARD ? destinationView : originView;
        View from = direction == Dispatcher.Direction.FORWARD ? originView : destinationView;
        ScreenTransition transition = transitions.findTransition(target, from);
        if (transition == null) {
            System.out.println("Cannot find transition for " + destinationView);
            callback.onTraversalCompleted();
            return;
        }

        AnimatorSet set = new AnimatorSet();
        set.setDuration(DURATION);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                callback.onTraversalCompleted();
            }
        });

        transition.configure(set);

        if (direction == Dispatcher.Direction.FORWARD) {
            transition.forward(destinationView, originView, set);
        } else {
            transition.backward(destinationView, originView, set);
        }

        set.start();
    }
}
