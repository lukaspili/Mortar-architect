package com.mortarnav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mortarnav.screen.ScreenA;
import com.mortarnav.view.ViewC;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Provides;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import mortarnav.library.Navigator;
import mortarnav.library.NavigatorContainerView;
import mortarnav.library.Transition;
import mortarnav.library.dagger.NavigatorInjector;
import mortarnav.library.transition.BottomAppearTransition;
import mortarnav.library.transition.HorizontalScreenTransition;


public class MainActivity extends Activity {

    private MortarScope scope;
    private Navigator navigator;

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
                    .navigatorModule(new NavigatorModule())
                    .build();
            component.inject(this);

            scope = MortarScope.buildChild(getApplicationContext())
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(DaggerService.SERVICE_NAME, component)
                    .build(getClass().getName());

            Navigator navigator = Navigator.create(scope);

            // option1: inject navigator transitions
            component.inject(navigator.transitions());

            // option2: register them directly
//            navigator.transitions()
//                    .register(Transition.defaultTransition(new HorizontalScreenTransition()))
//                    .register(Transition.forView(ViewB.class).fromAny().withTransition(new BottomAppearTransition()));
        }

        BundleServiceRunner.getBundleServiceRunner(this).onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        navigator = Navigator.find(this);
        navigator.delegate().onCreate(getIntent(), savedInstanceState, containerView, new ScreenA());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        navigator.delegate().onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundleServiceRunner.getBundleServiceRunner(this).onSaveInstanceState(outState);
        navigator.delegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        navigator.delegate().onDestroy();

        if (isFinishing() && scope != null) {
            scope.destroy();
            scope = null;

            navigator = null;
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

    @dagger.Component(dependencies = App.Component.class, modules = NavigatorModule.class)
    @DaggerScope(Component.class)
    public interface Component extends NavigatorInjector {

        void inject(MainActivity activity);
    }

    @dagger.Module
    public static class NavigatorModule {

        @Provides
        @DaggerScope(Component.class)
        public List<Transition> providesTransitions() {
            List<Transition> transitions = new ArrayList<>();
            transitions.add(Transition.defaultTransition(new HorizontalScreenTransition()));
            transitions.add(Transition.forView(ViewC.class).fromAny().withTransition(new BottomAppearTransition()));

            return transitions;
        }
    }
}
