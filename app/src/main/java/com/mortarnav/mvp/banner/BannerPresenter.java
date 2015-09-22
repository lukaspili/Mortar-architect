package com.mortarnav.mvp.banner;

import android.os.Bundle;

import com.mortarnav.DaggerScope;

import org.parceler.Parcel;

import java.util.Random;

import architect.robot.ScreenData;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@DaggerScope(BannerPresenter.class)
public class BannerPresenter extends ViewPresenter<BannerView> {

    @ScreenData
    private final BannerState state;

    public BannerPresenter(BannerState state) {
        this.state = state;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        Timber.d("Banner onLoad with random = %d", state.random);
    }

    public void click() {
        Timber.d("Clicked on banner!");
    }

    @Parcel(parcelsIndex = false)
    public static class BannerState {

        public BannerState() {
            Timber.d("NEW BannerState, random = %d", random);
        }

        int random = new Random().nextInt(100);
    }
}
