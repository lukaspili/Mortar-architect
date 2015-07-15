package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.DaggerScope;
import com.mortarnav.StandardAutoComponent;
import com.mortarnav.presenter.stackable.SlidePageStackable;
import com.mortarnav.view.SlidesView;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStackable(
        component = @AutoComponent(includes = StandardAutoComponent.class),
        pathWithView = SlidesView.class
//        pathWithLayout = R.layout.slides_view
)
@DaggerScope(SlidesPresenter.class)
public class SlidesPresenter extends ViewPresenter<SlidesView> {

    private SlidePageStackable[] pages;

    public SlidesPresenter() {
        pages = new SlidePageStackable[]{
                new SlidePageStackable(1),
                new SlidePageStackable(2),
                new SlidePageStackable(3)};
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);

        getView().show(pages);
    }
}
