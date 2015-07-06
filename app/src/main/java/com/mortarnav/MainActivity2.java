package com.mortarnav;

import com.mortarnav.deps.WithAppDependencies;
import com.mortarnav.path.HomePath;
import com.mortarnav.view.MyPopupView;

import architect.Navigator;
import architect.NavigatorView;
import architect.StackPath;
import architect.TransitionsMapping;
import architect.autostack.DaggerService;
import architect.commons.ArchitectActivity;
import architect.transition.Config;
import architect.transition.FadeModalTransition;
import architect.transition.LateralViewTransition;
import autodagger.AutoComponent;
import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.InjectView;
import mortar.MortarScope;

/**
 * Root activity example, using ArchitectActivity base class
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(
        dependencies = App2.class,
        superinterfaces = WithAppDependencies.class
)
@AutoInjector
@DaggerScope(MainActivity2.class)
public class MainActivity2 extends ArchitectActivity {

    @InjectView(R.id.navigator_container)
    protected NavigatorView containerView;

    @Override
    protected void createContentView() {
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @Override
    protected void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
        MainActivity2Component component = DaggerMainActivity2Component.builder()
                .app2Component(parentScope.<App2Component>getService(DaggerService.SERVICE_NAME))
                .build();
        component.inject(this);

        builder.withService(DaggerService.SERVICE_NAME, component);
    }

    @Override
    protected Navigator.Config getNavigatorConfig() {
        return null;
    }

    @Override
    protected NavigatorView getNavigatorView() {
        return containerView;
    }

    @Override
    protected TransitionsMapping getTransitionsMapping() {
        return new TransitionsMapping()
                .byDefault(new LateralViewTransition(new Config().duration(300)))
                .show(MyPopupView.class).withTransition(new FadeModalTransition(new Config().duration(250)));
    }

    @Override
    protected StackPath getInitialPath() {
        return new HomePath("Initial home");
    }
}
