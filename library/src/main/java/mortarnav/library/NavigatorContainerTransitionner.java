package mortarnav.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.View;

import mortarnav.library.transition.BottomAppearTransition;
import mortarnav.library.transition.HorizontalScreenTransition;
import mortarnav.library.transition.ModalTransition;
import mortarnav.library.transition.ScreenTransition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorContainerTransitionner {

    private static final int DURATION = 400;

    public void transition(View originView, View destinationView, Dispatcher.Direction direction, final Dispatcher.TraversalCallback callback) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(DURATION);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                callback.onTraversalCompleted();
            }
        });

        ScreenTransition transition = new BottomAppearTransition();
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
