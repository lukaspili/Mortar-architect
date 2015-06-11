package com.mortarnav.screen;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.mortarnav.DaggerScope;
import com.mortarnav.DaggerService;
import com.mortarnav.MainActivity;
import com.mortarnav.view.CustomViewA;
import com.mortarnav.view.ViewA;

import javax.inject.Inject;

import mortar.ViewPresenter;
import mortarnav.library.Navigator;
import mortarnav.library.ScreenContextFactory;
import mortarnav.library.Screen;
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
                .component(builderContext.getParentScope().<MainActivity.Component>getService(DaggerService.SERVICE_NAME))
                .build());
    }

    @dagger.Component(dependencies = MainActivity.Component.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(ViewA view);

        void inject(CustomViewA view);
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

        public void customViewClick() {
            System.out.println("Click from custom view A");
        }
    }
}
