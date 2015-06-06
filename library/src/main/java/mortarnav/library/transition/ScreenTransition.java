package mortarnav.library.transition;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ScreenTransition<T> {

    void enterTransition(T enterView, View exitView, AnimatorSet set);

    void exitTransition(T exitView, View enterView, AnimatorSet set);
}
