package com.mortarnav.nav;

import com.mortarnav.DaggerScope;
import com.mortarnav.DaggerService;
import com.mortarnav.MainActivity;
import com.mortarnav.view.BannerView;

import mortar.MortarScope;
import mortarnav.library.NavigationScope;
import mortarnav.library.NavigatorServices;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class BannerScope implements NavigationScope {

    @Override
    public Services withServices(MortarScope parentScope) {

        // parentScope is not the main activity scope, but the scope of its container (like home scope)
        // retreive the main activity component from the navigator scope
        MainActivity.Component component = NavigatorServices.getService(parentScope, DaggerService.SERVICE_NAME);

        return new Services().with(DaggerService.SERVICE_NAME, DaggerBannerScope_Component.builder()
                .component(component)
                .build());
    }

    @dagger.Component(dependencies = MainActivity.Component.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(BannerView view);
    }
}
