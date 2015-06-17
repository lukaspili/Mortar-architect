package com.mortarnav.presenter;

import com.mortarnav.DaggerScope;
import com.mortarnav.nav.SlidePageScope;
import com.mortarnav.view.SlidePageView;

import javax.inject.Inject;

import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@DaggerScope(SlidePageScope.Component.class)
public class SlidePagePresenter extends ViewPresenter<SlidePageView> {

    @Inject
    public SlidePagePresenter() {
    }


}
