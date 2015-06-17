package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.DaggerScope;
import com.mortarnav.nav.BannerScope;
import com.mortarnav.view.BannerView;

import javax.inject.Inject;

import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@DaggerScope(BannerScope.Component.class)
public class BannerPresenter extends ViewPresenter<BannerView> {

    @Inject
    public BannerPresenter() {

    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
    }

    public void click() {
        Timber.d("Clicked on banner!");
    }
}
