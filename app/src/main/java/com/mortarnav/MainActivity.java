package com.mortarnav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mortarnav.deps.WithAppDependencies;
import com.mortarnav.stackable.HomePath;
import com.mortarnav.view.MyPopupView;

import architect.Navigator;
import architect.NavigatorView;
import architect.TransitionsMapping;
import architect.commons.ActivityArchitector;
import architect.commons.Architected;
import architect.robot.DaggerService;
import architect.transition.Config;
import architect.transition.FadeModalTransition;
import architect.transition.LateralViewTransition;
import autodagger.AutoComponent;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;

/**
 * Root activity
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(
        dependencies = App.class,
        superinterfaces = WithAppDependencies.class
)
@AutoInjector
@DaggerScope(MainActivity.class)
public class MainActivity extends Activity {

    private MortarScope scope;
    private Navigator navigator;

    @Bind(R.id.navigator_container)
    protected NavigatorView containerView;

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

        scope = ActivityArchitector.onCreateScope(this, savedInstanceState, new Architected() {
            @Override
            public Navigator createNavigator(MortarScope scope) {
                Navigator navigator = Navigator.create(scope, new Parceler());
                navigator.transitions().register(new TransitionsMapping()
                        .byDefault(new LateralViewTransition(new Config().duration(300)))
                        .show(MyPopupView.class).withTransition(new FadeModalTransition(new Config().duration(250))));
                return navigator;
            }

            @Override
            public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
                MainActivityComponent component = DaggerMainActivityComponent.builder()
                        .appComponent(parentScope.<AppComponent>getService(DaggerService.SERVICE_NAME))
                        .build();
                component.inject(MainActivity.this);

                builder.withService(DaggerService.SERVICE_NAME, component);
            }
        });

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigator = ActivityArchitector.onCreateNavigator(this, savedInstanceState, containerView, new HomePath("Default home path"));
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
