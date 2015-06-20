package com.mortarnav.nav;

import com.mortarnav.DaggerScope;
import com.mortarnav.MainActivity;
import com.mortarnav.MainActivityComponent;

import autodagger.AutoComponent;
import mortar.MortarScope;
import mortarnav.NavigationScope;
import mortarnav.NavigatorServices;
import mortarnav.autoscope.DaggerService;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(dependencies = MainActivity.class)
@DaggerScope(BannerScope.class)
public class BannerScope implements NavigationScope {

    @Override
    public Services withServices(MortarScope parentScope) {

        // parentScope is not the main activity scope, but the scope of its container (like home scope)
        // retreive the main activity component from the navigator scope
        MainActivityComponent component = NavigatorServices.getService(parentScope, DaggerService.SERVICE_NAME);

        return new Services().with(DaggerService.SERVICE_NAME, DaggerBannerScopeComponent.builder()
                .mainActivityComponent(component)
                .build());
    }
}
