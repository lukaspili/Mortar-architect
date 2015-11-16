package architect.commons;

import android.app.Activity;
import android.os.Bundle;

import architect.Architect;
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
            final Architect architect = architected.createNavigator();

            MortarScope.Builder builder = parentScope.buildChild()
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(Architect.SERVICE_NAME, architect);
            architected.configureScope(builder, parentScope);
            scope = builder.build(scopeName);
            scope.register(architect);
        }

        BundleServiceRunner.getBundleServiceRunner(scope).onCreate(savedInstanceState);

        return scope;
    }

//    public static Architect onCreateNavigator(Activity activity, MortarScope scope, Bundle savedInstanceState, NavigationView architectView, Screen... defaultPaths) {
//        Architect architect = scope.getService(Architect.SERVICE_NAME);
//        architect.delegate().onCreate(activity.getIntent(), savedInstanceState, architectView, defaultPaths);
//        return architect;
//    }

}
