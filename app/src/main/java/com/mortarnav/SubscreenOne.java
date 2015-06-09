package com.mortarnav;

import android.os.Bundle;

import javax.inject.Inject;

import mortar.ViewPresenter;
import mortarnav.library.screen.ScreenContextFactory;
import mortarnav.library.screen.Subscreen;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SubscreenOne extends Subscreen {

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
