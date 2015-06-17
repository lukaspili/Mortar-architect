package com.mortarnav.nav;

import com.mortarnav.DaggerScope;
import com.mortarnav.DaggerService;
import com.mortarnav.MainActivity;
import com.mortarnav.view.SlidesView;

import mortar.MortarScope;
import mortarnav.library.NavigationScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SlidesScope implements NavigationScope {

    @Override
    public Services withServices(MortarScope parentScope) {
        return new Services().with(DaggerService.SERVICE_NAME, DaggerSlidesScope_Component.builder()
                .component(parentScope.<MainActivity.Component>getService(DaggerService.SERVICE_NAME))
                .build());
    }

    @dagger.Component(dependencies = MainActivity.Component.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(SlidesView view);
    }
}
