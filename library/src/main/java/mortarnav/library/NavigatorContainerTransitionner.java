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

//    private void screenTransition(View originView, View destinationView, Dispatcher.Direction direction, AnimatorSet set) {
//
//    }
//
//    private void modalTransition(final View originView, final View destinationView, final Dispatcher.Direction direction, AnimatorSet set) {
//        final ModalTransition transition = new BottomAppearTransition();
//        transition.configure(set);
//        set.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                if (direction == Dispatcher.Direction.BACKWARD && transition.hideViewBelow()) {
//                    destinationView.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                if (direction == Dispatcher.Direction.FORWARD && transition.hideViewBelow()) {
//                    originView.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        if (direction == Dispatcher.Direction.FORWARD) {
//            transition.show(destinationView, set);
//        } else {
//            transition.hide(originView, set);
//        }
//    }
}
