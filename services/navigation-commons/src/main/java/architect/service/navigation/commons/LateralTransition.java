package architect.service.navigation.commons;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import architect.Callback;
import architect.service.navigation.NavigationTransition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class LateralTransition implements NavigationTransition<View, View> {

    @Override
    public void forward(View enterView, View exitView, Callback callback) {
        performTransition(enterView, exitView, true, callback);
    }

    @Override
    public void backward(View enterView, View exitView, Callback callback) {
        performTransition(enterView, exitView, false, callback);
    }

    public void performTransition(View enterView, View exitView, boolean forward, final Callback callback) {
        final AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.setInterpolator(new AccelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                callback.onComplete();
            }
        });

        if (forward) {
            set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_X, enterView.getWidth(), 0));
            set.play(ObjectAnimator.ofFloat(exitView, View.TRANSLATION_X, 0, -exitView.getWidth()));
        } else {
            set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_X, -enterView.getWidth(), 0));
            set.play(ObjectAnimator.ofFloat(exitView, View.TRANSLATION_X, 0, exitView.getWidth()));
        }

        set.start();
    }
}
