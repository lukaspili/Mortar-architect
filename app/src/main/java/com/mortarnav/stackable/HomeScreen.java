package com.mortarnav.stackable;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mortarnav.DaggerScope;
import com.mortarnav.MainActivityComponent;
import com.mortarnav.deps.WithActivityDependencies;
import com.mortarnav.presenter.HomePresenter;
import com.mortarnav.view.HomeAdditionalCustomView;
import com.mortarnav.view.HomeView;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import architect.ScreenPath;
import architect.robot.DaggerService;
import dagger.Provides;
import mortar.MortarScope;

/**
 * Manually created StackablePath
 * You should rather use @AutoStackable if you can
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@Parcel(parcelsIndex = false)
public class HomeScreen implements ScreenPath {

    String name;

    @ParcelConstructor
    public HomeScreen(String name) {
        this.name = name;
    }

    @Override
    public View createView(Context context, ViewGroup parent) {
        return new HomeView(context);
    }

    @Override
    public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
        builder.withService(DaggerService.SERVICE_NAME, DaggerHomeScreen_Component.builder()
                .mainActivityComponent(parentScope.<MainActivityComponent>getService(DaggerService.SERVICE_NAME))
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

    @dagger.Component(dependencies = MainActivityComponent.class, modules = Module.class)
    @DaggerScope(Component.class)
    public interface Component extends WithActivityDependencies {

        void inject(HomeView view);

        void inject(HomeAdditionalCustomView view);
    }
}
