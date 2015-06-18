package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.DaggerScope;
import com.mortarnav.nav.SlidesScope;
import com.mortarnav.nav.path.SlidePagePath;
import com.mortarnav.view.SlidesView;

import javax.inject.Inject;

import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@DaggerScope(SlidesScope.Component.class)
public class SlidesPresenter extends ViewPresenter<SlidesView> {

    private SlidePagePath[] pagePaths;

    @Inject
    public SlidesPresenter() {
        pagePaths = new SlidePagePath[]{new SlidePagePath(1), new SlidePagePath(2), new SlidePagePath(3)};
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);

        getView().show(pagePaths);
    }
}
