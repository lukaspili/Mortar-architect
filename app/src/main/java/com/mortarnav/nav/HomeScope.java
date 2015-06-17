package com.mortarnav.nav;

import com.mortarnav.DaggerScope;
import com.mortarnav.DaggerService;
import com.mortarnav.MainActivity;
import com.mortarnav.presenter.HomePresenter;
import com.mortarnav.view.HomeAdditionalCustomView;
import com.mortarnav.view.HomeView;

import dagger.Provides;
import mortar.MortarScope;
import mortarnav.library.NavigationScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeScope implements NavigationScope {

    private final String name;

    public HomeScope(String name) {
        this.name = name;
    }

    @Override
    public Services withServices(MortarScope parentScope) {
        return new Services().with(DaggerService.SERVICE_NAME, DaggerHomeScope_Component.builder()
                .component(parentScope.<MainActivity.Component>getService(DaggerService.SERVICE_NAME))
                .module(new Module())
                .build());
    }

    @dagger.Module
    public class Module {

        @Provides
        @DaggerScope(Component.class)
        public HomePresenter providesPresenter() {
            return new HomePresenter(name);
        }
    }

    @dagger.Component(dependencies = MainActivity.Component.class, modules = Module.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(HomeView view);

        void inject(HomeAdditionalCustomView view);
    }
}
