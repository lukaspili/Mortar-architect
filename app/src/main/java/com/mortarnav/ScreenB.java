package com.mortarnav;

import android.content.Context;
import android.os.Bundle;

import javax.inject.Inject;

import mortar.MortarScope;
import mortar.ViewPresenter;
import mortarnav.library.Screen;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenB extends Screen<ViewB> {

    @Override
    public ViewB createView(Context context) {
        return new ViewB(context);
    }

    @Override
    public void configureScope(MortarScope parentScope, MortarScope.Builder builder) {
        builder.withService(DaggerService.SERVICE_NAME, DaggerScreenB_Component.builder()
                .component((MainActivity.Component) parentScope.getService(DaggerService.SERVICE_NAME))
                .build());
    }

    @dagger.Component(dependencies = MainActivity.Component.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(ViewB view);
    }

    @DaggerScope(Component.class)
    public static class Presenter extends ViewPresenter<ViewB> {

        @Inject
        public Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {

        }

        public void click() {

        }
    }
}
