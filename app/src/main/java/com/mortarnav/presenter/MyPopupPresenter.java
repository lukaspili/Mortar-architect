package com.mortarnav.presenter;

import com.mortarnav.DaggerScope;
import com.mortarnav.StandardAutoComponent;
import com.mortarnav.path.HomePath;
import com.mortarnav.presenter.scope.path.MyPopupPath;
import com.mortarnav.presenter.scope.path.SlidesPath;
import com.mortarnav.presenter.scope.path.SubnavPath;
import com.mortarnav.view.MyPopupView;

import architect.NavigationChain;
import architect.autopath.AutoPath;
import architect.autostack.AutoStack;
import architect.commons.ArchitectViewPresenter;
import autodagger.AutoComponent;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStack(
        component = @AutoComponent(includes = StandardAutoComponent.class),
        path = @AutoPath(withView = MyPopupView.class)
)
@DaggerScope(MyPopupPresenter.class)
public class MyPopupPresenter extends ArchitectViewPresenter<MyPopupView> {

    public void popupClick() {
        navigator().show(new MyPopupPath());
    }

    public void homeClick() {
        navigator().push(new HomePath("from popup"));
    }

    public void dismissClick() {
        navigator().chain(new NavigationChain()
                .back()
                .push(new SubnavPath())
                .push(new SlidesPath()));
    }
}
