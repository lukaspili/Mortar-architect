package com.mortarnav.nav;

import com.mortarnav.DaggerScope;
import com.mortarnav.DaggerService;
import com.mortarnav.MainActivity;
import com.mortarnav.view.SubnavView;

import mortar.MortarScope;
import mortarnav.NavigationScope;
import mortarnav.autopath.AutoPath;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoPath(withView = SubnavView.class)
public class SubnavScope implements NavigationScope {

    @Override
    public Services withServices(MortarScope parentScope) {
        return new Services().with(DaggerService.SERVICE_NAME, DaggerSubnavScope_Component.builder()
                .component(parentScope.<MainActivity.Component>getService(DaggerService.SERVICE_NAME))
                .build());
    }

    @dagger.Component(dependencies = MainActivity.Component.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(SubnavView view);
    }
}
