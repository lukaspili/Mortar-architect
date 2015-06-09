package mortarnav.library.transition;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Horizontal left to write transition
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HorizontalScreenTransition implements ScreenTransition {

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

    @Override
    public void configure(AnimatorSet set) {
        set.setInterpolator(new AccelerateDecelerateInterpolator());
    }
}
