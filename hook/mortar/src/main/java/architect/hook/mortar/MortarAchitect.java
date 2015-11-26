package architect.hook.mortar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import architect.Architect;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class MortarAchitect {

    public static final String SERVICE_NAME = Architect.class.getName();

    public static MortarScope onCreateScope(Activity activity, Bundle savedInstanceState, Factory factory) {
        final String scopeName = activity.getLocalClassName() + "-task-" + activity.getTaskId();
        MortarScope scope = MortarScope.findChild(activity.getApplicationContext(), scopeName);
        if (scope == null) {
            final MortarScope parentScope = MortarScope.getScope(activity.getApplicationContext());
            final Architect architect = factory.createArchitect();

            MortarScope.Builder builder = parentScope.buildChild()
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(SERVICE_NAME, architect);
            factory.configureScope(builder, parentScope);
            scope = builder.build(scopeName);

            factory.configureArchitectWithMortar(architect, scope);
        }

        BundleServiceRunner.getBundleServiceRunner(scope).onCreate(savedInstanceState);

        return scope;
    }

    public static Architect get(Context context) {
        //noinspection ResourceType
        return (Architect) context.getSystemService(SERVICE_NAME);
    }

    public static Architect get(View view) {
        return get(view.getContext());
    }

    public interface Factory extends HandlesMortarScope {

        Architect createArchitect();

        void configureArchitectWithMortar(Architect architect, MortarScope scope);
    }
}
