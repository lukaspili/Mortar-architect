package com.mortarnav.presenter;

import com.mortarnav.DaggerScope;
import com.mortarnav.StandardAutoComponent;
import com.mortarnav.presenter.stackable.MyPopup2Stackable;
import com.mortarnav.view.MyPopup2View;


import architect.Navigator;
import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStackable(
        component = @AutoComponent(includes = StandardAutoComponent.class),
        pathWithView = MyPopup2View.class
)
@DaggerScope(MyPopup2Presenter.class)
public class MyPopup2Presenter extends ViewPresenter<MyPopup2View> {

    public void popupClick() {
        Navigator.get(getView()).show(new MyPopup2Stackable());
    }

    public void dismissClick() {
        Navigator.get(getView()).backToRoot();
    }
}
