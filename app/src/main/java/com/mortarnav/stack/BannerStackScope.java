package com.mortarnav.stack;

import com.mortarnav.DaggerScope;
import com.mortarnav.MainActivity2;
import com.mortarnav.MainActivity2Component;

import autodagger.AutoComponent;
import mortar.MortarScope;
import architect.StackScope;
import architect.NavigatorServices;
import architect.autostack.DaggerService;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(dependencies = MainActivity2.class)
@DaggerScope(BannerStackScope.class)
public class BannerStackScope implements StackScope {

    @Override
    public Services withServices(MortarScope parentScope) {

        // parentScope is not the main activity scope, but the scope of its container (like home scope)
        // retreive the main activity component from the navigator scope
        MainActivity2Component component = NavigatorServices.getService(parentScope, DaggerService.SERVICE_NAME);

        return new Services().with(DaggerService.SERVICE_NAME, DaggerBannerStackScopeComponent.builder()
                .mainActivity2Component(component)
                .build());
    }
}
