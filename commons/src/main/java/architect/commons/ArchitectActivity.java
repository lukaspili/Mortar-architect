package architect.commons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import architect.StackPath;
import architect.Navigator;
import architect.NavigatorView;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class ArchitectActivity extends Activity {

    protected MortarScope scope;
    protected Navigator navigator;

    protected abstract void createContentView();

    protected abstract void configureScope(MortarScope.Builder builder, MortarScope parMortarScope);

    protected abstract void configureNavigator(Navigator navigator);

    protected abstract NavigatorView getNavigatorView();

    protected abstract StackPath getInitialPath();

    @Override
    public Object getSystemService(String name) {
        if (scope != null && scope.hasService(name)) {
            return scope.getService(name);
        }

        return super.getSystemService(name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String scopeName = getLocalClassName() + "-task-" + getTaskId();
        scope = MortarScope.findChild(getApplicationContext(), scopeName);
        if (scope == null) {
            MortarScope parentScope = MortarScope.getScope(getApplicationContext());

            MortarScope.Builder builder = parentScope.buildChild()
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner());
            configureScope(builder, parentScope);
            scope = builder.build(scopeName);

            Navigator navigator = Navigator.create(scope);
            configureNavigator(navigator);
        }

        BundleServiceRunner.getBundleServiceRunner(scope).onCreate(savedInstanceState);

        createContentView();

        navigator = Navigator.find(this);
        navigator.delegate().onCreate(getIntent(), savedInstanceState, getNavigatorView(), getInitialPath());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        navigator.delegate().onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(scope).onSaveInstanceState(outState);
        navigator.delegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigator.delegate().onStart();
    }

    @Override
    protected void onStop() {
        navigator.delegate().onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        navigator.delegate().onDestroy();
        navigator = null;

        if (isFinishing() && scope != null) {
            scope.destroy();
            scope = null;
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (navigator.delegate().onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }
}
