package architect.commons.transition;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import architect.ViewTransition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class StandardTransition extends AbstractViewTransition<View, View> {

    public StandardTransition() {
    }

    public StandardTransition(Config config) {
        super(config);
    }

    @Override
    public void performTransition(View enterView, View exitView, int direction, AnimatorSet set) {
        super.performTransition(enterView, exitView, direction, set);

        if (direction == ViewTransition.DIRECTION_FORWARD) {
            set.play(ObjectAnimator.ofFloat(enterView, View.ALPHA, .0f, 1.f).setDuration(200));
            set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_Y, 8.f, 0.f).setDuration(350));
            set.play(ObjectAnimator.ofFloat(exitView, View.ALPHA, 1.f, .7f).setDuration(217));
        } else {
//            set.play(ObjectAnimator.ofFloat(enterView, View.ALPHA, .0f, 1.f).setDuration(200));
//            set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_Y, 8.f, 0.f).setDuration(350));
//            set.play(ObjectAnimator.ofFloat(exitView, View.ALPHA, 1.f, .7f).setDuration(217));
        }
    }
}
