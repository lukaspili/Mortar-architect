package com.mortarnav.presenter;

import com.mortarnav.DaggerScope;
import com.mortarnav.view.HomeDoubleNestedView;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStackable(
        component = @AutoComponent(dependencies = HomeNestedPresenter.class)
)
@DaggerScope(HomeDoubleNestedPresenter.class)
public class HomeDoubleNestedPresenter extends ViewPresenter<HomeDoubleNestedView> {


}
