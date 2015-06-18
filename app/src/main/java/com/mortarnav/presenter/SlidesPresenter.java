package com.mortarnav.presenter;

import com.mortarnav.DaggerScope;
import com.mortarnav.nav.SlidesScope;
import com.mortarnav.view.SlidesView;

import javax.inject.Inject;

import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@DaggerScope(SlidesScope.Component.class)
public class SlidesPresenter extends ViewPresenter<SlidesView> {

    @Inject
    public SlidesPresenter() {
    }


}
