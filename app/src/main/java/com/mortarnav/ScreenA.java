package com.mortarnav;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import mortar.ViewPresenter;
import mortarnav.library.Navigator;
import mortarnav.library.screen.Screen;
import mortarnav.library.screen.ScreenContextFactory;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenA extends Screen {

    @Override
    public View createView(Context context) {
        return new ViewA(context);
    }

    @Override
    public void configureMortarScope(ScreenContextFactory.BuilderContext builderContext) {
        builderContext.getScopeBuilder().withService(DaggerService.SERVICE_NAME, DaggerScreenA_Component.builder()
                .component((MainActivity.Component) builderContext.getParentScope().getService(DaggerService.SERVICE_NAME))
                .build());
    }

    @dagger.Component(dependencies = MainActivity.Component.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(ViewA view);
    }

    @DaggerScope(Component.class)
    public static class Presenter extends ViewPresenter<ViewA> {

        @Inject
        public Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.d("Presenter A onLoad %s", this);
        }

        public void click() {
            Navigator.get(getView().getContext()).push(new ScreenB("Hello Lukasz"));
        }
    }
}
