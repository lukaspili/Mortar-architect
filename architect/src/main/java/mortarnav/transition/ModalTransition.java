package mortarnav.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class ModalTransition<T extends View> implements ScreenTransition<View, T> {

    @Override
    public final void forward(T enterView, final View exitView, AnimatorSet set) {
        if (hideViewBelow()) {
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    exitView.setVisibility(View.GONE);
                }
            });
        }

        show(enterView, set);
    }

    @Override
    public final void backward(final View enterView, T exitView, AnimatorSet set) {
        if (hideViewBelow()) {
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    enterView.setVisibility(View.VISIBLE);
                }
            });
        }

        hide(exitView, set);
    }

    public abstract void show(T view, AnimatorSet set);

    public abstract void hide(T view, AnimatorSet set);

    public abstract boolean hideViewBelow();
}
