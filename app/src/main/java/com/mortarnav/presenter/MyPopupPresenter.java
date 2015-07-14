package com.mortarnav.presenter;

import com.mortarnav.DaggerScope;
import com.mortarnav.StandardAutoComponent;
import com.mortarnav.presenter.stackable.MyPopup2Stackable;
import com.mortarnav.presenter.stackable.MyPopupStackable;
import com.mortarnav.presenter.stackable.SlidesStackable;
import com.mortarnav.presenter.stackable.SubnavStackable;
import com.mortarnav.view.MyPopupView;

import architect.NavigationChain;
import architect.Navigator;
import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStackable(
        component = @AutoComponent(includes = StandardAutoComponent.class),
        pathWithView = MyPopupView.class
)
@DaggerScope(MyPopupPresenter.class)
public class MyPopupPresenter extends ViewPresenter<MyPopupView> {

    public void popupClick() {
        Navigator.get(getView()).show(new MyPopupStackable());
    }

    public void dismissClick() {
        Navigator.get(getView()).chain(new NavigationChain()
                .backToRoot()
                .push(new SubnavStackable())
                .push(new SlidesStackable()));
    }

    public void showPopup2Click() {
        Navigator.get(getView()).chain(new NavigationChain()
                .back()
                .show(new MyPopup2Stackable()));
    }
}
