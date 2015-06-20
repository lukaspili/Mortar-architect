//package com.mortarnav.nav;
//
//import com.mortarnav.DaggerScope;
//import com.mortarnav.DaggerService;
//import com.mortarnav.presenter.SubnavPagePresenter;
//import com.mortarnav.view.SubnavPageView;
//
//import dagger.Provides;
//import mortar.MortarScope;
//import mortarnav.NavigationScope;
//import mortarnav.autopath.AutoPath;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//@AutoPath(withView = SubnavPageView.class)
//public class SubnavPageScope implements NavigationScope {
//
//    private String title;
//
//    public SubnavPageScope(String title) {
//        this.title = title;
//    }
//
//    @Override
//    public Services withServices(MortarScope parentScope) {
//        return new Services().with(DaggerService.SERVICE_NAME, DaggerSubnavPageScope_Component.builder()
//                .component(parentScope.<SubnavScope.Component>getService(DaggerService.SERVICE_NAME))
//                .module(new Module())
//                .build());
//    }
//
//    @dagger.Module
//    public class Module {
//
//        @Provides
//        @DaggerScope(Component.class)
//        public SubnavPagePresenter providesPresenter() {
//            return new SubnavPagePresenter(title);
//        }
//    }
//
//    @dagger.Component(dependencies = SubnavScope.Component.class, modules = Module.class)
//    @DaggerScope(Component.class)
//    public interface Component {
//
//        void inject(SubnavPageView view);
//    }
//}
