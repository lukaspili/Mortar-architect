package architect.examples.mortar_app;

import android.app.Application;

import mortar.MortarScope;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class App extends Application {

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

        scope = MortarScope.buildRootScope().build("Root");
    }
}
