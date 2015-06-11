package com.mortarnav.screen;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.mortarnav.DaggerScope;
import com.mortarnav.DaggerService;
import com.mortarnav.view.ViewOne;

import javax.inject.Inject;

import mortar.ViewPresenter;
import mortarnav.library.ScreenContextFactory;
import mortarnav.library.Screen;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SubscreenOne extends Screen {

    public SubscreenOne() {
    }

    @Override
    public View createView(Context context) {
        return new ViewOne(context);
    }

    @Override
    public void configureMortarScope(ScreenContextFactory.BuilderContext builderContext) {
        builderContext.getScopeBuilder().withService(DaggerService.SERVICE_NAME, DaggerSubscreenOne_Component.builder()
                .component(builderContext.getParentScope().<ScreenA.Component>getService(DaggerService.SERVICE_NAME))
                .build());
    }

    @dagger.Component(dependencies = ScreenA.Component.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(ViewOne view);
    }

    @DaggerScope(Component.class)
    public static class Presenter extends ViewPresenter<ViewOne> {

        @Inject
        public Presenter() {
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            Timber.d("Presenter SubscreenOne onLoad %s", this);
        }
    }
}
