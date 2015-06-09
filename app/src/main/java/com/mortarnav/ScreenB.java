package com.mortarnav;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import dagger.Provides;
import mortar.ViewPresenter;
import mortarnav.library.Navigator;
import mortarnav.library.Screen;
import mortarnav.library.ScreenContextFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenB extends Screen {

    private String name;

    public ScreenB(String name) {
        this.name = name;
    }

    @Override
    public View createView(Context context) {
        return new ViewB(context);
    }

    @Override
    public void configureMortarScope(ScreenContextFactory.BuilderContext builderContext) {
        builderContext.getScopeBuilder().withService(DaggerService.SERVICE_NAME, DaggerScreenB_Component.builder()
                .component((MainActivity.Component) builderContext.getParentScope().getService(DaggerService.SERVICE_NAME))
                .module(new Module())
                .build());
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(Component.class)
        public Presenter providesPresenter() {
            return new Presenter(name);
        }
    }

    @dagger.Component(modules = Module.class, dependencies = MainActivity.Component.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(ViewB view);
    }

    public static class Presenter extends ViewPresenter<ViewB> {

        private String name;

        public Presenter(String name) {
            this.name = name;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            getView().configure(name);
        }

        public void click() {
            Navigator.get(getView().getContext()).push(new ScreenC());
        }
    }
}
