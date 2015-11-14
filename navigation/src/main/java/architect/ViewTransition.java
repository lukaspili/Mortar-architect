package architect;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ViewTransition<T_Enter extends View, T_Exit extends View> {

    int DIRECTION_FORWARD = 1;
    int DIRECTION_BACKWARD = 2;

    void performTransition(T_Enter enterView, T_Exit exitView, int direction, AnimatorSet set);
}
