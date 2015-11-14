package architect.commons.transition;

import android.animation.AnimatorSet;
import android.view.View;

import architect.Transition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class AbstractViewTransition<T_Enter extends View, T_Exit extends View> implements Transition<T_Enter, T_Exit> {

    protected Config config;

    public AbstractViewTransition() {
        this(new Config());
    }

    public AbstractViewTransition(Config config) {
        this.config = config;
    }

    @Override
    public void performTransition(T_Enter enterView, T_Exit exitView, int direction, AnimatorSet set) {
        config.configure(set);
    }
}
