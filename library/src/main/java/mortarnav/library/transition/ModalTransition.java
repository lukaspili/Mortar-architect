package mortarnav.library.transition;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ModalTransition<T extends View> {

    void show(T view, AnimatorSet set);

    void hide(T view, AnimatorSet set);

    void configure(AnimatorSet set);

    boolean hideViewBelow();
}
