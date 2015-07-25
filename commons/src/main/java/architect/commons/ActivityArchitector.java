package architect.commons;

import android.app.Activity;
import android.os.Bundle;

import architect.Navigator;
import architect.NavigatorView;
import architect.ScreenPath;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ActivityArchitector {

    public static MortarScope onCreateScope(Activity activity, Bundle savedInstanceState, Architected architected) {
        String scopeName = activity.getLocalClassName() + "-task-" + activity.getTaskId();
        MortarScope scope = MortarScope.findChild(activity.getApplicationContext(), scopeName);
        if (scope == null) {
            MortarScope parentScope = MortarScope.getScope(activity.getApplicationContext());

            MortarScope.Builder builder = parentScope.buildChild()
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner());
            architected.configureScope(builder, parentScope);
            scope = builder.build(scopeName);

            architected.createNavigator(scope);
        }

        BundleServiceRunner.getBundleServiceRunner(scope).onCreate(savedInstanceState);

        return scope;
    }

    public static Navigator onCreateNavigator(Activity activity, Bundle savedInstanceState, NavigatorView navigatorView, ScreenPath... defaultPaths) {
        Navigator navigator = Navigator.find(activity);
        navigator.delegate().onCreate(activity.getIntent(), savedInstanceState, navigatorView, defaultPaths);
        return navigator;
    }

}
