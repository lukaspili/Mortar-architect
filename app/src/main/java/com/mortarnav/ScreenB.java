package com.mortarnav;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import mortar.ViewPresenter;
import mortarnav.library.Screen;
import mortarnav.library.context.ScreenContextBuilder;
import mortarnav.library.ScreenContextFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenB extends Screen implements ScreenContextBuilder {

    @Override
    public View createView(Context context) {
        return new ViewB(context);
    }

    @Override
    public void buildScreenScope(ScreenContextFactory.BuilderContext builderContext) {
        builderContext.getScopeBuilder().withService(DaggerService.SERVICE_NAME, DaggerScreenB_Component.builder()
                .component((MainActivity.Component) builderContext.getParentScope().getService(DaggerService.SERVICE_NAME))
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
