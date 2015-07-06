package architect.commons;

import android.view.View;

import architect.Navigator;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class ArchitectViewPresenter<V extends View> extends ViewPresenter<V> {

    protected Navigator navigator() {
        return Navigator.get(getView());
    }


}
