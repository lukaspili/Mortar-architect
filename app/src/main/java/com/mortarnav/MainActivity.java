package com.mortarnav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mortarnav.deps.WithActivityDependencies;
import com.mortarnav.stackable.HomeScreen;

import javax.inject.Inject;

import architect.Navigator;
import architect.NavigatorView;
import architect.TransitionsMapping;
import architect.commons.ActivityArchitector;
import architect.commons.Architected;
import architect.commons.transition.Config;
import architect.commons.transition.LateralViewTransition;
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

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setTitle("Mortar architect");
        setSupportActionBar(toolbar);

        scope = ActivityArchitector.onCreateScope(this, savedInstanceState, new Architected() {
            @Override
            public Navigator createNavigator(MortarScope scope) {
                Navigator navigator = Navigator.create(scope, new Parceler());
                navigator.transitions().register(new TransitionsMapping()
                                .byDefault(new LateralViewTransition(new Config().duration(300)))
//                                .show(MyPopupView.class).withTransition(new FadeModalTransition(new Config().duration(250)))
//                                .show(MyPopup2View.class).withTransition(new BottomAppearTransition(true, new Config().duration(1000)))
//                                .show(SlidesView.class).withTransition(new CustomFullScreenLateralTransition())
                );
                return navigator;
            }

            @Override
            public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
                MainActivityComponent component = DaggerMainActivityComponent.builder()
                        .appComponent(parentScope.<AppComponent>getService(DaggerService.SERVICE_NAME))
                        .build();
                builder.withService(DaggerService.SERVICE_NAME, component);
            }
        });

        DaggerService.<MainActivityComponent>get(this).inject(this);
        toolbarOwner.takeView(toolbar);

        // it is usually the best to create the navigator after everything else
        navigator = ActivityArchitector.onCreateNavigator(this, savedInstanceState, containerView, new HomeScreen("Default home path"));
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
