package architect.transition;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ViewTransition<T_Origin extends View, T_Destination extends View> {

    void forward(T_Destination enterView, T_Origin exitView, AnimatorSet set);

    void backward(T_Origin enterView, T_Destination exitView, AnimatorSet set);

    void configure(AnimatorSet set);

    boolean removeExitView();
}
