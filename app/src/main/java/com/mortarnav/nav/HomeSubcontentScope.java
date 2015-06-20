//package com.mortarnav.nav;
//
//import com.mortarnav.DaggerScope;
//
//import autodagger.AutoComponent;
//import mortar.MortarScope;
//import mortarnav.NavigationScope;
//import mortarnav.autoscope.DaggerService;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//@AutoComponent(dependencies = HomeScope.Component.class)
//@DaggerScope(HomeSubcontentScope.class)
//public class HomeSubcontentScope implements NavigationScope {
//
//    @Override
//    public Services withServices(MortarScope parentScope) {
//        return new Services().with(DaggerService.SERVICE_NAME, DaggerHomeSubcontentScopeComponent.builder()
//                .component(parentScope.<HomeScope.Component>getService(DaggerService.SERVICE_NAME))
//                .build());
//    }
//
////    @dagger.Component(dependencies = HomeScope.Component.class)
////    @DaggerScope(HomeSubcontentScope.class)
////    public interface Component {
////
//////        void inject(HomeSubcontentView view);
////    }
//}
