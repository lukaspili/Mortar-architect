package com.mortarnav;

import android.app.Application;

import autodagger.AutoComponent;
import mortar.MortarScope;
import architect.autostack.DaggerService;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent
@DaggerScope(App.class)
public class App extends Application {

    //    private AppComponent component;
    private MortarScope scope;

    @Override
    public Object getSystemService(String name) {
        return (scope != null && scope.hasService(name)) ? scope.getService(name) : super.getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        AppComponent component = DaggerAppComponent.create();

        scope = MortarScope.buildRootScope()
                .withService(DaggerService.SERVICE_NAME, component)
                .build("Root");
    }

//    public static AppComponent getComponent(Application application) {
//        return ((App) application).component;
//    }
}
