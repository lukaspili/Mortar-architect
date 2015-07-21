package architect;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ViewTransition<T_Enter extends View, T_Exit extends View> {

    void transition(T_Enter enterView, T_Exit exitView, ViewTransitionDirection direction, AnimatorSet set);
}
