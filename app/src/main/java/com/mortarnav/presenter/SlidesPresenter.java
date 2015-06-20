package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.DaggerScope;
import com.mortarnav.MainActivity;
import com.mortarnav.presenter.scope.path.SlidePagePath;
import com.mortarnav.view.SlidesView;

import javax.inject.Inject;

import autodagger.AutoComponent;
import mortar.ViewPresenter;
import mortarnav.autopath.AutoPath;
import mortarnav.autoscope.AutoScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoScope(
        component = @AutoComponent(dependencies = MainActivity.Component.class),
        path = @AutoPath(withView = SlidesView.class)
)
@DaggerScope(SlidesPresenter.class)
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
