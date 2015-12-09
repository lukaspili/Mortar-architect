package architect.service.navigation;

import android.view.View;

import architect.Callback;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface NavigationTransition<T_Enter extends View, T_Exit extends View> {

    int DIRECTION_FORWARD = 1;
    int DIRECTION_BACKWARD = 2;

    void forward(T_Enter enterView, T_Exit exitView, Callback callback);

    void backward(T_Enter enterView, T_Exit exitView, Callback callback);
}