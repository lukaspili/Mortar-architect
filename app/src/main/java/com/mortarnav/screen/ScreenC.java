//package com.mortarnav.screen;
//
//import android.os.Bundle;
//import android.view.View;
//
//import com.mortarnav.DaggerScope;
//import com.mortarnav.DaggerService;
//import com.mortarnav.MainActivity;
//import com.mortarnav.view.ViewC;
//
//import javax.inject.Inject;
//
//import mortar.MortarScope;
//import mortar.ViewPresenter;
//import mortarnav.library.ScreenScope;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class ScreenC implements ScreenScope {
//
//    @Override
//    public Class<? extends View> withView() {
//        return ViewC.class;
//    }
//
//    @Override
//    public Services withServices(MortarScope parentScope) {
//        return new Services().with(DaggerService.SERVICE_NAME, DaggerScreenC_Component.builder()
//                .component(parentScope.<MainActivity.Component>getService(DaggerService.SERVICE_NAME))
//                .build());
//    }
//
//    @dagger.Component(dependencies = MainActivity.Component.class)
//    @DaggerScope(Component.class)
//    public interface Component {
//
//        void inject(ViewC view);
//    }
//
//    @DaggerScope(Component.class)
//    public static class Presenter extends ViewPresenter<ViewC> {
//
//        @Inject
//        public Presenter() {
//        }
//
//        @Override
//        protected void onLoad(Bundle savedInstanceState) {
//
//        }
//
//        public void click() {
//
//        }
//    }
//}
