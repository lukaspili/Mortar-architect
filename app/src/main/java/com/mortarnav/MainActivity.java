package com.mortarnav;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.mortarnav.deps.WithActivityDependencies;
import com.mortarnav.mvp.home.HomeScreen;

import architect.Navigator;
import architect.NavigatorView;
import architect.commons.ActivityArchitector;
import architect.commons.Architected;
import architect.commons.transition.StandardTransition;
import architect.robot.DaggerService;
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
        superinterfaces = WithActivityDependencies.class
)
@AutoInjector
@DaggerScope(MainActivity.class)
public class MainActivity extends AppCompatActivity {

    private MortarScope scope;
    private Navigator navigator;

    @Bind(R.id.navigator_container)
    protected NavigatorView containerView;

    @Override
    public Object getSystemService(@NonNull String name) {
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
            public Navigator createNavigator() {
                Navigator navigator = new Navigator(new Parceler());
                navigator.transitions().setDefault(new StandardTransition());
                return navigator;
            }

            @Override
            public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
                DaggerService.configureScope(builder, MainActivity.class, DaggerMainActivityComponent.builder()
                        .appComponent(parentScope.<AppComponent>getService(DaggerService.SERVICE_NAME))
                        .build());
            }
        });

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DaggerService.<MainActivityComponent>get(this).inject(this);

        // it is usually the best to create the navigator after everything else
        navigator = ActivityArchitector.onCreateNavigator(this, scope, savedInstanceState, containerView, new HomeScreen("Default home path"));
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