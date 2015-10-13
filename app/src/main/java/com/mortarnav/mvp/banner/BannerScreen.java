package com.mortarnav.mvp.banner;

import com.mortarnav.DaggerScope;
import com.mortarnav.MainActivity;
import com.mortarnav.MainActivityComponent;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import architect.Screen;
import architect.robot.DaggerService;
import autodagger.AutoComponent;
import dagger.Provides;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(
        modules = BannerScreen.Module.class,
        dependencies = MainActivity.class,
        target = BannerPresenter.class)
@DaggerScope(BannerPresenter.class)
@Parcel(parcelsIndex = false)
public class BannerScreen implements Screen {

    BannerPresenter.BannerState bannerState;

    public BannerScreen() {
        bannerState = new BannerPresenter.BannerState();
    }

    @ParcelConstructor
    BannerScreen(BannerPresenter.BannerState bannerState) {
        this.bannerState = bannerState;
    }

    @Override
    public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
        DaggerService.configureScope(builder, BannerScreen.class, DaggerBannerScreenComponent.builder()
                .module(new Module())
                .mainActivityComponent(DaggerService.<MainActivityComponent>getTyped(parentScope, MainActivity.class))
                .build());
    }

    @dagger.Module
    public class Module {

        @Provides
        @DaggerScope(BannerPresenter.class)
        public BannerPresenter providesPresenter() {
            return new BannerPresenter(bannerState);
        }
    }

//    public interface StateWithBannerScreen {
//
//        BannerScreen getBannerScreen();
//    }
}
