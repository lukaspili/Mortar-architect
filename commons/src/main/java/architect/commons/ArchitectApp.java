package architect.commons;

import android.app.Application;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class ArchitectApp extends Application {

    protected MortarScope scope;

    protected abstract void configureScope(MortarScope.Builder builder);

    @Override
    public Object getSystemService(String name) {
        return (scope != null && scope.hasService(name)) ? scope.getService(name) : super.getSystemService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MortarScope.Builder builder = MortarScope.buildRootScope();
        configureScope(builder);
        scope = builder.build("Root");
    }


}
