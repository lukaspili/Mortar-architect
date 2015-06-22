package architect.transition;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class BottomAppearTransition extends BaseModalTransition {

    public BottomAppearTransition() {
    }

    public BottomAppearTransition(Config config) {
        super(config);
    }

    @Override
    public void show(View view, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.getHeight(), 0));
    }

    @Override
    public void hide(View view, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, view.getHeight()));
    }

    @Override
    public boolean hideViewBelow() {
        return true;
    }
}
