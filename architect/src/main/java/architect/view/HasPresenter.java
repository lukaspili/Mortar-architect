package architect.view;

import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface HasPresenter<T extends ViewPresenter> {

    T getPresenter();
}
