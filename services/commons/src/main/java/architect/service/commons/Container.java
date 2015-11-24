package architect.service.commons;

import android.view.View;

/**
 * Created by lukasz on 23/11/15.
 */
public interface Container<T extends View> extends HandlesBack {

    T getView();

    void willBeginTransition();

    void didEndTransition();
}
