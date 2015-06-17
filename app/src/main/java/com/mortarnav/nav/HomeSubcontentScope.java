package com.mortarnav.nav;

import com.mortarnav.DaggerScope;
import com.mortarnav.DaggerService;
import com.mortarnav.view.HomeSubcontentView;

import mortar.MortarScope;
import mortarnav.library.NavigationScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeSubcontentScope implements NavigationScope {

    @Override
    public Services withServices(MortarScope parentScope) {
        return new Services().with(DaggerService.SERVICE_NAME, DaggerHomeSubcontentScope_Component.builder()
                .component(parentScope.<HomeScope.Component>getService(DaggerService.SERVICE_NAME))
                .build());
    }

    @dagger.Component(dependencies = HomeScope.Component.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(HomeSubcontentView view);
    }
}
