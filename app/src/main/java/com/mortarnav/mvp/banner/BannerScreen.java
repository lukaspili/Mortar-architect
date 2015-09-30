package com.mortarnav.mvp.banner;

import com.mortarnav.DaggerScope;
import com.mortarnav.MainActivity;
import com.mortarnav.MainActivityComponent;

import org.parceler.Parcel;

import architect.NavigatorServices;
import architect.Screen;
import architect.robot.RobotService;
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

    @Override
    public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
        // parentScope is not the main activity scope, but the scope of its container (like home scope)
        // retreive the main activity component from the navigator scope
        MainActivityComponent component = NavigatorServices.getService(parentScope, RobotService.SERVICE_NAME);

        builder.withService(RobotService.SERVICE_NAME, DaggerBannerScreenComponent.builder()
                .module(new Module())
                .mainActivityComponent(component)
                .build());

        if (bannerState == null) {
            bannerState = new BannerPresenter.BannerState();
        }

//        if (bannerState == null) {
//            bannerState = new BannerPresenter.BannerState();
//            Timber.d("Put banner state");
//        }
//        builder.withService(ScreenService.SERVICE_NAME, bannerState);
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
