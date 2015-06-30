package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.DaggerScope;
import com.mortarnav.StandardAutoComponent;
import com.mortarnav.presenter.scope.path.SlidePagePath;
import com.mortarnav.view.SlidesView;

import javax.inject.Inject;

import architect.autopath.AutoPath;
import architect.autostack.AutoStack;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStack(
        component = @AutoComponent(includes = StandardAutoComponent.class),
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
