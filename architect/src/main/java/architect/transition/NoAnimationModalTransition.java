package architect.transition;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * Modal transition that is not animated
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NoAnimationModalTransition extends BaseModalTransition<View> {

    private boolean hideExitView;

    /**
     * @param hideExitView should the transition hide the exit view?
     */
    public NoAnimationModalTransition(boolean hideExitView) {
        this.hideExitView = hideExitView;
    }

    @Override
    public void configure(AnimatorSet set) {
        set.setDuration(0);
    }

    @Override
    public void show(View view, AnimatorSet set) {
    }

    @Override
    public void hide(View view, AnimatorSet set) {
    }

    @Override
    public boolean hideExitView() {
        return hideExitView;
    }
}
