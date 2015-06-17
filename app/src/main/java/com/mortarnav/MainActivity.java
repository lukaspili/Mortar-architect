package com.mortarnav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mortarnav.path.HomePath;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Provides;
import mortar.MortarScope;
import mortar.bundler.BundleServiceRunner;
import mortarnav.library.Navigator;
import mortarnav.library.NavigatorView;
import mortarnav.library.Transition;
import mortarnav.library.Transitions;
import mortarnav.library.transition.Config;
import mortarnav.library.transition.HorizontalScreenTransition;


public class MainActivity extends Activity {

    private MortarScope scope;
    private Navigator navigator;

    @InjectView(R.id.navigator_container)
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

        String scopeName = getLocalClassName() + "-task-" + getTaskId();
        scope = MortarScope.findChild(getApplicationContext(), scopeName);
        if (scope == null) {
            Component component = DaggerMainActivity_Component.builder()
                    .component(App.getComponent(getApplication()))
                    .navigatorModule(new NavigatorModule())
                    .build();
            component.inject(this);

            scope = MortarScope.buildChild(getApplicationContext())
                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
                    .withService(DaggerService.SERVICE_NAME, component)
                    .build(scopeName);

            Navigator navigator = Navigator.create(scope);

            // option1: inject navigator transitions
            component.inject(navigator.transitions());

            // option2: register them directly
//            navigator.transitions()
//                    .register(Transition.defaultTransition(new HorizontalScreenTransition()))
//                    .register(Transition.forView(ViewB.class).fromAny().withTransition(new BottomAppearTransition()));
        }

        BundleServiceRunner.getBundleServiceRunner(scope).onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        navigator = Navigator.find(this);
        navigator.delegate().onCreate(getIntent(), savedInstanceState, containerView, new HomePath("Initial home"));
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

    @dagger.Component(dependencies = App.Component.class, modules = NavigatorModule.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(MainActivity activity);

        void inject(Transitions transitions);
    }

    @dagger.Module
    public static class NavigatorModule {

        @Provides
        @DaggerScope(Component.class)
        public List<Transition> providesTransitions() {
            List<Transition> transitions = new ArrayList<>();
            transitions.add(Transition.defaultTransition(new HorizontalScreenTransition(new Config().duration(300))));
//            transitions.add(Transition.forView(ViewC.class).fromAny().withTransition(new BottomAppearTransition()));

            return transitions;
        }
    }
}
