package com.mortarnav.presenter;

import com.mortarnav.DaggerScope;
import com.mortarnav.StandardAutoComponent;
import com.mortarnav.view.MyPopupView;

import architect.autopath.AutoPath;
import architect.autostack.AutoStack;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStack(
        component = @AutoComponent(includes = StandardAutoComponent.class),
        path = @AutoPath(withView = MyPopupView.class)
)
@DaggerScope(MyPopupPresenter.class)
public class MyPopupPresenter extends ViewPresenter<MyPopupView> {

    
}
