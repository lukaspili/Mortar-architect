package com.mortarnav.mvp.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mortarnav.DaggerScope;
import com.mortarnav.DaggerService;
import com.mortarnav.MainActivity;
import com.mortarnav.MainActivityComponent;
import com.mortarnav.deps.WithActivityDependencies;
import com.mortarnav.mvp.banner.BannerScreen;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import architect.ScreenPath;
import architect.SubScreenService;
import architect.nav.ReceivesNavigationResult;
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
    BannerScreen bannerScreen2;

    String name;
    String result;

    public HomeScreen(String name) {
        this.name = name;
        state = new HomePresenter.HomeState();
        bannerScreen = new BannerScreen();
        bannerScreen2 = new BannerScreen();
    }

    @ParcelConstructor
    HomeScreen(HomePresenter.HomeState state, BannerScreen bannerScreen, BannerScreen bannerScreen2, String name) {
        this.state = state;
        this.bannerScreen = bannerScreen;
        this.bannerScreen2 = bannerScreen2;
        this.name = name;
    }

    @Override
    public View createView(Context context, ViewGroup parent) {
        return new HomeView(context);
    }

    @Override
    public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
        DaggerService.configureScope(builder, HomeScreen.class, DaggerHomeScreen_Component.builder()
                .mainActivityComponent(DaggerService.<MainActivityComponent>getTyped(parentScope, MainActivity.class))
                .module(new Module())
                .build());

        builder.withService(SubScreenService.SERVICE_NAME, new SubScreenService.Builder()
                .withScreen("bannerScreen", bannerScreen)
                .withScreen("bannerScreen2", bannerScreen2)
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
            return new HomePresenter(state, name, result);
        }
    }

    @dagger.Component(dependencies = MainActivityComponent.class, modules = Module.class)
    @DaggerScope(Component.class)
    public interface Component extends WithActivityDependencies {

        void inject(HomeView view);

        void inject(HomeAdditionalCustomView view);
    }
}
