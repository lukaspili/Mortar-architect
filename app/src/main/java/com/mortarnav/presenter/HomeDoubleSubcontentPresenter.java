package com.mortarnav.presenter;

import com.mortarnav.DaggerScope;
import com.mortarnav.MainActivity2;
import com.mortarnav.MainActivity2Component;
import com.mortarnav.view.HomeDoubleSubcontentView;

import javax.inject.Inject;

import architect.NavigatorServices;
import architect.StackScope;
import architect.autostack.DaggerService;
import autodagger.AutoComponent;
import mortar.MortarScope;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
//@AutoStack(
//        component = @AutoComponent(dependencies = HomeSubcontentPresenter.class)
//)
@DaggerScope(HomeDoubleSubcontentPresenter.class)
public class HomeDoubleSubcontentPresenter extends ViewPresenter<HomeDoubleSubcontentView> {

    @Inject
    public HomeDoubleSubcontentPresenter() {
        Timber.d("NEW %s", this);
    }

    @AutoComponent(
            dependencies = MainActivity2.class,
            target = HomeDoubleSubcontentPresenter.class
    )
    @DaggerScope(HomeDoubleSubcontentPresenter.class)
    public static class HomeDoubleSubcontentScope implements StackScope {

        @Override
        public StackScope.Services withServices(MortarScope parentScope) {
            MainActivity2Component component = NavigatorServices.getService(parentScope, DaggerService.SERVICE_NAME);
            return new StackScope.Services().with(DaggerService.SERVICE_NAME, DaggerHomeDoubleSubcontentScopeComponent.builder()
                    .mainActivity2Component(component)
                    .build());
        }
    }
}
