package com.mortarnav.presenter;

import com.mortarnav.DaggerScope;
import com.mortarnav.StandardAutoComponent;
import com.mortarnav.view.ReturnsResultView;

import architect.Navigator;
import architect.autopath.AutoPath;
import architect.autostack.AutoStack;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStack(
        component = @AutoComponent(includes = StandardAutoComponent.class),
        path = @AutoPath(withView = ReturnsResultView.class)
)
@DaggerScope(ReturnsResultPresenter.class)
public class ReturnsResultPresenter extends ViewPresenter<ReturnsResultView> {

    public void clickTwo() {
        Navigator.get(getView()).back("Result TWO");
    }

    public void clickOne() {
        Navigator.get(getView()).back("Result ONE");
    }


}
