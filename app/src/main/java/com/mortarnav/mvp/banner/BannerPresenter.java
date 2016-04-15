package com.mortarnav.mvp.banner;

import android.os.Bundle;

import com.mortarnav.MainActivity;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Random;

import architect.robot.AutoScreen;
import architect.robot.ScreenData;
import autodagger.AutoComponent;
import mortar.ViewPresenter;
import nz.bradcampbell.paperparcel.PaperParcel;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoScreen(
        component = @AutoComponent(dependencies = MainActivity.class)
)
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

    @PaperParcel
    public static class BannerState {

        int random;

        public BannerState() {
            this.random = new Random().nextInt(100);
        }
    }
}
