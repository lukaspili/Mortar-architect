package architect.commons.transition;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import architect.ViewTransition;

/**
 * Left / right transition
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class LateralViewTransition implements ViewTransition {

    protected Config config;

    public LateralViewTransition() {
        this(new Config().duration(300).interpolator(new AccelerateDecelerateInterpolator()));
    }

    public LateralViewTransition(Config config) {
        this.config = config;
    }

    @Override
    public void performTransition(View enterView, View exitView, int direction, AnimatorSet set) {
        config.configure(set);

        if (direction == ViewTransition.DIRECTION_FORWARD) {
            set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_X, enterView.getWidth(), 0));
            set.play(ObjectAnimator.ofFloat(exitView, View.TRANSLATION_X, 0, -exitView.getWidth()));
        } else {
            set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_X, -enterView.getWidth(), 0));
            set.play(ObjectAnimator.ofFloat(exitView, View.TRANSLATION_X, 0, exitView.getWidth()));
        }
    }
}
