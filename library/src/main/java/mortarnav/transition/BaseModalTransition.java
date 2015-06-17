package mortarnav.transition;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class BaseModalTransition<T extends View> extends ModalTransition<T> {

    protected final Config config;

    public BaseModalTransition() {
        this(new Config());
    }

    public BaseModalTransition(Config config) {
        this.config = config;
    }

    @Override
    public void configure(AnimatorSet set) {
        config.configure(set);
    }
}
