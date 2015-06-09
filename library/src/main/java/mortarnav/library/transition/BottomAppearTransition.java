package mortarnav.library.transition;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class BottomAppearTransition implements ModalTransition<View> {

    @Override
    public void show(View view, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.getHeight(), 0));
    }

    @Override
    public void hide(View view, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, view.getHeight()));
    }

    @Override
    public void configure(AnimatorSet set) {
        set.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    public boolean hideViewBelow() {
        return true;
    }
}
