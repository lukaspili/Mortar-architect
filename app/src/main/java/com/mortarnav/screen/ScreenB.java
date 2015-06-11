package com.mortarnav.screen;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.mortarnav.DaggerScope;
import com.mortarnav.DaggerService;
import com.mortarnav.MainActivity;
import com.mortarnav.view.ViewB;

import dagger.Provides;
import mortar.MortarScope;
import mortar.ViewPresenter;
import mortarnav.library.Navigator;
import mortarnav.library.NavigatorTransition;
import mortarnav.library.ScreenContextFactory;
import mortarnav.library.screen.Screen;
import mortarnav.library.transition.HorizontalScreenTransition;
import timber.log.Timber;

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
        private Navigator navigator;

        public Presenter(String name) {
            this.name = name;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            getView().configure(name);

            navigator = Navigator.find(getView().getContext());
            Timber.d("Find navigator: %s", navigator);
            if (navigator == null) {
                navigator = Navigator.create(MortarScope.getScope(getView().getContext()));
                navigator.transitions().register(NavigatorTransition.defaultTransition(new HorizontalScreenTransition()));
            }

            navigator.delegate().onCreate(null, savedInstanceState, getView().containerView, new ScreenBSubPageScreen("One!"));
        }

        @Override
        protected void onSave(Bundle outState) {
            navigator.delegate().onSaveInstanceState(outState);
        }

        @Override
        public void dropView(ViewB view) {
            navigator.delegate().onDestroy();
            navigator = null;

            super.dropView(view);
        }

        public boolean backPressed() {
            return navigator.delegate().onBackPressed();
        }

        public void click() {
            Navigator.get(getView().getContext()).push(new ScreenC());
        }
    }
}
