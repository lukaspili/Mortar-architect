package com.mortarnav;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import mortarnav.library.Navigator;
import mortarnav.library.NavigatorContainerView;
import timber.log.Timber;


public class MainActivity extends Activity {

    private MortarScope scope;

    @InjectView(R.id.navigator_container)
    protected NavigatorContainerView containerView;

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

        scope = MortarScope.getScope(this);

        if (scope == null) {
            Component component = DaggerMainActivity_Component.builder()
                    .component(App.getComponent(getApplication()))
                    .build();
            component.inject(this);

            Navigator navigator = new Navigator();

            scope = MortarScope.buildChild(getApplicationContext())
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(DaggerService.SERVICE_NAME, component)
                    .withService(Navigator.SERVICE_NAME, navigator)
                    .build(getClass().getName());
        }

        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        Navigator.get(this).delegate().onCreate(containerView, new ScreenA());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        Navigator.get(this).delegate().onDestroy();

        if (isFinishing()) {
            MortarScope activityScope = MortarScope.getScope(this);
            if (activityScope != null) {
                Timber.d("Destroy activity scope");
                activityScope.destroy();
            }
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (Navigator.get(this).delegate().onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }

    @dagger.Component(dependencies = App.Component.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(MainActivity activity);
    }
}
