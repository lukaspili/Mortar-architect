package mortarnav;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.View;

import mortarnav.transition.ScreenTransition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class TransitionExecutor {

    private final Transitions transitions;

    public TransitionExecutor(Transitions transitions) {
        this.transitions = transitions;
    }

    public void makeTransition(View originView, View destinationView, Dispatcher.Direction direction, final Presenter.PresenterSession session, final Presenter.TransitionCallback callback) {
        ScreenTransition transition = findTransition(originView, destinationView, direction);
        if (transition == null) {
            callback.onTransitionFinished(session);
            return;
        }

        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                callback.onTransitionFinished(session);
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

    private ScreenTransition findTransition(View originView, View destinationView, Dispatcher.Direction direction) {
        // depending on transition direction, the target view is either the origin or destination
        View target = direction == Dispatcher.Direction.FORWARD ? destinationView : originView;
        View from = direction == Dispatcher.Direction.FORWARD ? originView : destinationView;
        return transitions.findTransition(target, from);
    }
}
