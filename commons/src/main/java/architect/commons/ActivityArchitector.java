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
        final String scopeName = activity.getLocalClassName() + "-task-" + activity.getTaskId();
        MortarScope scope = MortarScope.findChild(activity.getApplicationContext(), scopeName);
        if (scope == null) {
            final MortarScope parentScope = MortarScope.getScope(activity.getApplicationContext());
            final Navigator navigator = architected.createNavigator();

            MortarScope.Builder builder = parentScope.buildChild()
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(Navigator.SERVICE_NAME, navigator);
            architected.configureScope(builder, parentScope);
            scope = builder.build(scopeName);
            scope.register(navigator);
        }

        BundleServiceRunner.getBundleServiceRunner(scope).onCreate(savedInstanceState);

        return scope;
    }

    public static Navigator onCreateNavigator(Activity activity, MortarScope scope, Bundle savedInstanceState, NavigatorView navigatorView, ScreenPath... defaultPaths) {
        Navigator navigator = scope.getService(Navigator.SERVICE_NAME);
        navigator.delegate().onCreate(activity.getIntent(), savedInstanceState, navigatorView, defaultPaths);
        return navigator;
    }

}
