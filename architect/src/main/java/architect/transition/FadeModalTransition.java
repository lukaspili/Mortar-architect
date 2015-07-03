package architect.transition;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class FadeModalTransition extends BaseModalTransition<View> {

    private boolean hideExitView;

    /**
     * @param hideExitView should the transition hide the exit view?
     */
    public FadeModalTransition(boolean hideExitView) {
        this.hideExitView = hideExitView;
    }

    @Override
    public void configure(AnimatorSet set) {

    }

    @Override
    public void show(View view, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1));
    }

    @Override
    public void hide(View view, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(view, View.ALPHA, 1, 0));
    }

    @Override
    public boolean hideExitView() {
        return hideExitView;
    }
}
