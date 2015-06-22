package architect.transition;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Horizontal left to write transition
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HorizontalScreenTransition extends BaseScreenTransition {

    public HorizontalScreenTransition() {

    }

    public HorizontalScreenTransition(Config config) {
        super(config);
    }

    @Override
    public void forward(View enterView, View exitView, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_X, enterView.getWidth(), 0));
        set.play(ObjectAnimator.ofFloat(exitView, View.TRANSLATION_X, 0, -exitView.getWidth()));
    }

    @Override
    public void backward(View enterView, View exitView, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_X, -enterView.getWidth(), 0));
        set.play(ObjectAnimator.ofFloat(exitView, View.TRANSLATION_X, 0, exitView.getWidth()));
    }
}
