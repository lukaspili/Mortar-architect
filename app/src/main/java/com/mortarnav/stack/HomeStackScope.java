package com.mortarnav.stack;

import com.mortarnav.DaggerScope;
import com.mortarnav.MainActivity2Component;
import com.mortarnav.presenter.HomePresenter;
import com.mortarnav.view.HomeAdditionalCustomView;
import com.mortarnav.view.HomeView;

import dagger.Provides;
import mortar.MortarScope;
import architect.StackScope;
import architect.autostack.DaggerService;

/**
 * Manually written scope, for example purpose
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeStackScope implements StackScope {

    private final String name;

    public HomeStackScope(String name) {
        this.name = name;
    }

    @Override
    public Services withServices(MortarScope parentScope) {
        return new Services().with(DaggerService.SERVICE_NAME, DaggerHomeStackScope_Component.builder()
                .mainActivity2Component(parentScope.<MainActivity2Component>getService(DaggerService.SERVICE_NAME))
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

    @dagger.Component(dependencies = MainActivity2Component.class, modules = Module.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(HomeView view);

        void inject(HomeAdditionalCustomView view);
    }
}
