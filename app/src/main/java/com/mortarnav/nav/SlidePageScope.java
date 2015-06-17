package com.mortarnav.nav;

import com.mortarnav.DaggerScope;
import com.mortarnav.DaggerService;
import com.mortarnav.view.SlidePageView;

import mortar.MortarScope;
import mortarnav.library.NavigationScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SlidePageScope implements NavigationScope {

    @Override
    public Services withServices(MortarScope parentScope) {
        return new Services().with(DaggerService.SERVICE_NAME, DaggerSlidePageScope_Component.builder()
                .component(parentScope.<SlidesScope.Component>getService(DaggerService.SERVICE_NAME))
                .build());
    }

    @dagger.Component(dependencies = SlidesScope.Component.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(SlidePageView view);
    }
}
