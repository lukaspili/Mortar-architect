package com.mortarnav.mvp.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mortarnav.DaggerScope;
import com.mortarnav.MainActivityComponent;
import com.mortarnav.deps.WithActivityDependencies;
import com.mortarnav.mvp.banner.BannerScreen;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import architect.ScreenPath;
import architect.commons.ScreenService;
import architect.robot.DaggerService;
import architect.screen.ReceivesNavigationResult;
import dagger.Provides;
import mortar.MortarScope;

/**
 * Manually created Screen
 * You should rather use @AutoStackable if you can
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@Parcel(parcelsIndex = false)
public class HomeScreen implements ScreenPath, ReceivesNavigationResult<String> {

    HomePresenter.HomeState state;
    BannerScreen bannerScreen;
    String name;
    String result;

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

        if (state == null) {
            state = new HomePresenter.HomeState();
        }

        if (bannerScreen == null) {
            bannerScreen = new BannerScreen();
        }
        builder.withService(ScreenService.SERVICE_NAME, new ScreenService.Builder()
                .withScreen(BannerScreen.class, bannerScreen)
                .build());
    }

    @Override
    public void onReceiveNavigationResult(String result) {
        this.result = result;
    }

    @dagger.Module
    public class Module {

        @Provides
        @DaggerScope(Component.class)
        public HomePresenter providesPresenter() {
            if (result == null) {
                return new HomePresenter(name, state);
            } else {
                return new HomePresenter(name, state, result);
            }
        }
    }

    @dagger.Component(dependencies = MainActivityComponent.class, modules = Module.class)
    @DaggerScope(Component.class)
    public interface Component extends WithActivityDependencies {

        void inject(HomeView view);

        void inject(HomeAdditionalCustomView view);
    }
}
