package com.mortarnav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mortarnav.deps.WithActivityDependencies;
import com.mortarnav.mvp.home.HomeScreen;

import javax.inject.Inject;

import architect.Navigator;
import architect.NavigatorView;
import architect.commons.ActivityArchitector;
import architect.commons.Architected;
import architect.commons.transition.LateralViewTransition;
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

    @Inject
    protected ToolbarOwner toolbarOwner;

    @Bind(R.id.navigator_container)
    protected NavigatorView containerView;

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

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
            public Navigator createNavigator() {
                Navigator navigator = new Navigator(new Parceler());
                navigator.transitions().setDefault(new LateralViewTransition());
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

        toolbar.setTitle("Mortar architect");
        setSupportActionBar(toolbar);

        DaggerService.<MainActivityComponent>get(this).inject(this);
        toolbarOwner.takeView(toolbar);

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
        toolbarOwner.dropView(toolbar);

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
