package mortarnav.library.transition;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class BaseScreenTransition<T_Origin extends View, T_Destination extends View> implements ScreenTransition<T_Origin, T_Destination> {

    protected final Config config;

    public BaseScreenTransition() {
        this(new Config());
    }

    public BaseScreenTransition(Config config) {
        this.config = config;
    }

    @Override
    public void configure(AnimatorSet set) {
        config.configure(set);
    }
}
