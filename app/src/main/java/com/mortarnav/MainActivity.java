package com.mortarnav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import mortarnav.library.Navigator;
import mortarnav.library.NavigatorContainerView;
import mortarnav.library.NavigatorTransition;
import mortarnav.library.transition.BottomAppearTransition;
import mortarnav.library.transition.HorizontalScreenTransition;


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

        scope = MortarScope.findChild(getApplicationContext(), getClass().getName());

        if (scope == null) {
            Component component = DaggerMainActivity_Component.builder()
                    .component(App.getComponent(getApplication()))
                    .build();
            component.inject(this);

            Navigator navigator = new Navigator();
            navigator.transitions()
                    .register(NavigatorTransition.defaultTransition(new HorizontalScreenTransition()))
                    .register(NavigatorTransition.forView(ViewB.class).fromAny().withTransition(new BottomAppearTransition()));

            scope = MortarScope.buildChild(getApplicationContext())
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(DaggerService.SERVICE_NAME, component)
                    .withService(Navigator.SERVICE_NAME, navigator)
                    .build(getClass().getName());
        }

        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        Navigator.get(this).delegate().onCreate(getIntent(), savedInstanceState, containerView, new ScreenA());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Navigator.get(this).delegate().onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
        Navigator.get(this).delegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        Navigator.get(this).delegate().onDestroy();

        if (isFinishing() && scope != null) {
            scope.destroy();
            scope = null;
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
