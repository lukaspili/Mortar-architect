package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.DaggerScope;
import com.mortarnav.view.BannerView;

import org.parceler.Parcel;

import java.util.Random;

import architect.commons.ScreenService;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@DaggerScope(BannerPresenter.class)
public class BannerPresenter extends ViewPresenter<BannerView> {

    private BannerState state;

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        state = ScreenService.get(getView().getScreenContext());
        if (state.random == -1) {
            state.random = new Random().nextInt(100);
        }

        Timber.d("Banner presenter with random: %d", state.random);
    }

    public void click() {
        Timber.d("Clicked on banner!");
    }

    @Parcel(parcelsIndex = false)
    public static class BannerState {

        int random = -1;
    }
}
