package com.mortarnav;

import android.app.Activity;
import android.os.Bundle;

import butterknife.InjectView;
import mortar.MortarScope;
import mortarnav.library.Navigator;
import mortarnav.library.NavigatorContainerView;


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
        setContentView(R.layout.activity_main);

        scope = MortarScope.findChild(this, getClass().getName());

        if (scope == null) {
            Component component = DaggerMainActivity_Component.builder()
                    .component(App.getComponent(getApplication()))
                    .build();
            component.inject(this);

            Navigator navigator = new Navigator();

            scope = MortarScope.buildChild(this)
                    .withService(DaggerService.SERVICE_NAME, component)
                    .withService(Navigator.SERVICE_NAME, navigator)
                    .build(getClass().getName());
        }

        Navigator.get(this).delegate().onCreate(containerView, new ScreenA());
    }

    @dagger.Component(dependencies = App.Component.class)
    public interface Component {

        void inject(MainActivity activity);
    }
}
