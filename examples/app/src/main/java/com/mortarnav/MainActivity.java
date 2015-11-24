package com.mortarnav;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import architect.examples.mortar_app.deps.WithActivityDependencies;

import architect.Architect;
import architect.robot.dagger.DaggerScope;
import architect.robot.dagger.DaggerService;
import autodagger.AutoComponent;
import autodagger.AutoInjector;
import butterknife.ButterKnife;

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

//    private MortarScope scope;
    private Architect architect;

//    @Bind(R.id.navigator_container)
//    protected NavigationView containerView;

//    @Override
//    public Object getSystemService(@NonNull String name) {
//        if (scope != null && scope.hasService(name)) {
//            return scope.getService(name);
//        }
//
//        return super.getSystemService(name);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        scope = ActivityArchitector.onCreateScope(this, savedInstanceState, new Architected() {
//            @Override
//            public Architect createNavigator() {
//                Architect architect = new Architect(new Parceler());
//                architect.transitions().setPushDefault(new StandardTransition());
//                return architect;
//            }
//
//            @Override
//            public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
//                DaggerService.configureScope(builder, MainActivity.class, DaggerMainActivityComponent.builder()
//                        .appComponent(parentScope.<AppComponent>getService(DaggerService.SERVICE_NAME))
//                        .build());
//            }
//        });

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DaggerService.<MainActivityComponent>get(this).inject(this);

        // it is usually the best to create the navigator after everything else
        architect = new Architect(new Parceler());
        architect.delegate().onCreate(getIntent(), savedInstanceState);
//        architect = ActivityArchitector.onCreateNavigator(this, scope, savedInstanceState, containerView, new HomeScreen("Default home path"));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        architect.delegate().onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        BundleServiceRunner.getBundleServiceRunner(scope).onSaveInstanceState(outState);
        architect.delegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        architect.delegate().onStart();
    }

    @Override
    protected void onStop() {
        architect.delegate().onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        architect.delegate().onDestroy();
        architect = null;

//        if (isFinishing() && scope != null) {
//            scope.destroy();
//            scope = null;
//        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (architect.delegate().onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }
}