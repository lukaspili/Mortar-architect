package architect.service.navigation.commons;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import architect.Callback;
import architect.service.navigation.NavigationTransition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ActivityTransition implements NavigationTransition<View, View> {

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
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                callback.onComplete();
            }
        });

        float translateY = exitView.getHeight() * 8 / 100;
        if (forward) {
            Animator animator1 = ObjectAnimator.ofFloat(enterView, View.ALPHA, .0f, 1.f).setDuration(200);
            animator1.setInterpolator(new DecelerateInterpolator(2.0f));
            set.play(animator1);

            Animator animator2 = ObjectAnimator.ofFloat(enterView, View.TRANSLATION_Y, translateY, 0.f).setDuration(350);
            animator2.setInterpolator(new DecelerateInterpolator(2.5f));
            set.play(animator2);

            Animator animator3 = ObjectAnimator.ofFloat(exitView, View.ALPHA, 1.f, .7f).setDuration(217);
            animator3.setInterpolator(new FastOutSlowInInterpolator());
            set.play(animator3);
        } else {
            Animator animator1 = ObjectAnimator.ofFloat(enterView, View.ALPHA, .7f, 1.f).setDuration(250);
            animator1.setInterpolator(new LinearOutSlowInInterpolator());
            set.play(animator1);

            Animator animator2 = ObjectAnimator.ofFloat(exitView, View.TRANSLATION_Y, 0.f, translateY).setDuration(250);
            animator2.setInterpolator(new AccelerateInterpolator(2.0f));
            set.play(animator2);

            Animator animator3 = ObjectAnimator.ofFloat(exitView, View.ALPHA, 1.f, .0f).setDuration(150);
            animator3.setInterpolator(new LinearInterpolator());
            animator3.setStartDelay(100);
            set.play(animator3);
        }

        set.start();
    }
}
