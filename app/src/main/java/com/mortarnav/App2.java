package com.mortarnav;

import autodagger.AutoComponent;
import mortar.MortarScope;
import mortarnav.autoscope.DaggerService;
import mortarnav.commons.ArchitectApp;
import timber.log.Timber;

/**
 * App with ArchitectApp base class
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent
@DaggerScope(App2.class)
public class App2 extends ArchitectApp {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    @Override
    protected void configureScope(MortarScope.Builder builder) {
        App2Component component = DaggerApp2Component.create();
        builder.withService(DaggerService.SERVICE_NAME, component);
    }
}
