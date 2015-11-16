package architect.examples.simple_app.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import architect.Callback;
import architect.service.presentation.Transition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class BottomSlideTransition implements Transition {

    @Override
    public void show(View view, Callback callback) {
        perform(view, false, callback);
    }

    @Override
    public void hide(View view, Callback callback) {
        perform(view, true, callback);
    }

    private void perform(View view, boolean reverse, final Callback callback) {
        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                callback.onComplete();
            }
        });

        set.play(ObjectAnimator.ofFloat(view, View.Y, reverse ? view.getHeight() : 0, reverse ? 0 : view.getHeight()));
        set.start();
    }
}
