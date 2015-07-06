package architect.transition;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class BottomAppearTransition extends BaseModalTransition {

    protected boolean hideExitView = true;

    public BottomAppearTransition() {
    }

    public BottomAppearTransition(Config config) {
        super(config);
    }

    /**
     * @param hideExitView should the transition hide the exit view?
     */
    public BottomAppearTransition(boolean hideExitView) {
        this.hideExitView = hideExitView;
    }

    /**
     * @param hideExitView should the transition hide the exit view?
     */
    public BottomAppearTransition(boolean hideExitView, Config config) {
        super(config);
        this.hideExitView = hideExitView;
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
    public boolean hideExitView() {
        return hideExitView;
    }
}
