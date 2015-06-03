package com.mortarnav;

import android.app.Application;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class App extends Application {

    private Component component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApp_Component.create();

        MortarScope scope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, component)
                .build("Root");
    }

    public static Component getComponent(Application application) {
        return ((App) application).component;
    }

    @dagger.Component
    public interface Component {

    }
}
