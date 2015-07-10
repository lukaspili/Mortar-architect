package com.mortarnav.stackable;

import com.mortarnav.DaggerScope;
import com.mortarnav.MainActivity;
import com.mortarnav.MainActivityComponent;
import com.mortarnav.presenter.BannerPresenter;

import architect.NavigatorServices;
import architect.Stackable;
import architect.robot.DaggerService;
import autodagger.AutoComponent;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(
        dependencies = MainActivity.class,
        target = BannerPresenter.class)
@DaggerScope(BannerPresenter.class)
public class BannerStackable implements Stackable {

    @Override
    public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
        // parentScope is not the main activity scope, but the scope of its container (like home scope)
        // retreive the main activity component from the navigator scope
        MainActivityComponent component = NavigatorServices.getService(parentScope, DaggerService.SERVICE_NAME);

        builder.withService(DaggerService.SERVICE_NAME, DaggerBannerStackableComponent.builder()
                .mainActivityComponent(component)
                .build());
    }
}
