package com.mortarnav.nav;

import com.mortarnav.DaggerScope;
import com.mortarnav.DaggerService;
import com.mortarnav.presenter.SlidePagePresenter;
import com.mortarnav.view.SlidePageView;

import dagger.Provides;
import mortar.MortarScope;
import mortarnav.NavigationScope;
import mortarnav.autopath.AutoPath;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoPath(withView = SlidePageView.class)
public class SlidePageScope implements NavigationScope {

    private int id;

    public SlidePageScope(int id) {
        this.id = id;
    }

    @Override
    public Services withServices(MortarScope parentScope) {
        return new Services().with(DaggerService.SERVICE_NAME, DaggerSlidePageScope_Component.builder()
                .component(parentScope.<SlidesScope.Component>getService(DaggerService.SERVICE_NAME))
                .module(new Module())
                .build());
    }

    @dagger.Module
    public class Module {

        @Provides
        @DaggerScope(Component.class)
        public SlidePagePresenter providesPresenter() {
            return new SlidePagePresenter(id);
        }
    }

    @dagger.Component(dependencies = SlidesScope.Component.class, modules = Module.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(SlidePageView view);
    }
}
