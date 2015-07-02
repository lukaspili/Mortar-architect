package architect.transition;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class BaseViewTransition<T_Origin extends View, T_Destination extends View> implements ViewTransition<T_Origin, T_Destination> {

    protected final Config config;

    public BaseViewTransition() {
        this(new Config());
    }

    public BaseViewTransition(Config config) {
        this.config = config;
    }

    @Override
    public void configure(AnimatorSet set) {
        config.configure(set);
    }

    @Override
    public boolean removeExitView() {
        return true;
    }
}
