package architect.commons.transition;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import architect.Transition;

/**
 * Left / right transition
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class LateralTransition extends AbstractViewTransition<View, View> {

    public LateralTransition() {
    }

    public LateralTransition(Config config) {
        super(config);
    }

    @Override
    public void performTransition(View enterView, View exitView, int direction, AnimatorSet set) {
        super.performTransition(enterView, exitView, direction, set);

        if (direction == Transition.DIRECTION_FORWARD) {
            set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_X, enterView.getWidth(), 0));
            set.play(ObjectAnimator.ofFloat(exitView, View.TRANSLATION_X, 0, -exitView.getWidth()));
        } else {
            set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_X, -enterView.getWidth(), 0));
            set.play(ObjectAnimator.ofFloat(exitView, View.TRANSLATION_X, 0, exitView.getWidth()));
        }
    }
}
