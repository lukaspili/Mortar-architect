package architect.examples.mortar_app.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import architect.Callback;
import architect.service.show.Transition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class BottomSlideTransition implements Transition {

    @Override
    public void show(View view, Callback callback) {
        perform(view, true, callback);
    }

    @Override
    public void hide(View view, Callback callback) {
        perform(view, false, callback);
    }

    private void perform(View view, boolean forward, final Callback callback) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(3000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                callback.onComplete();
            }
        });

        set.play(ObjectAnimator.ofFloat(view, View.Y, forward ? view.getHeight() : 0, forward ? 0 : view.getHeight()));
        set.start();
    }
}
