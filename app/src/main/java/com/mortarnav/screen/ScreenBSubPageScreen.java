//package com.mortarnav.screen;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//
//import com.mortarnav.DaggerScope;
//import com.mortarnav.DaggerService;
//import com.mortarnav.view.ScreenBSubPageView;
//
//import dagger.Provides;
//import mortar.ViewPresenter;
//import mortarnav.library.Navigator;
//import mortarnav.library.ScreenContextFactory;
//import mortarnav.library.ScreenScope;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class ScreenBSubPageScreen extends ScreenScope {
//
//    private String name;
//
//    public ScreenBSubPageScreen(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public View createView(Context context) {
//        return new ScreenBSubPageView(context);
//    }
//
//    @Override
//    public void configureMortarScope(ScreenContextFactory.BuilderContext builderContext) {
//        builderContext.getScopeBuilder().withService(DaggerService.SERVICE_NAME, DaggerScreenBSubPageScreen_Component.builder()
//                .component(builderContext.getParentScope().<ScreenB.Component>getService(DaggerService.SERVICE_NAME))
//                .module(new Module())
//                .build());
//    }
//
//    @dagger.Module
//    public class Module {
//
//        @Provides
//        @DaggerScope(Component.class)
//        public Presenter providesPresenter() {
//            return new Presenter(name);
//        }
//    }
//
//    @dagger.Component(modules = Module.class, dependencies = ScreenB.Component.class)
//    @DaggerScope(Component.class)
//    public interface Component {
//
//        void inject(ScreenBSubPageView view);
//    }
//
//    public static class Presenter extends ViewPresenter<ScreenBSubPageView> {
//
//        private final String name;
//
//        public Presenter(String name) {
//            this.name = name;
//        }
//
//        @Override
//        protected void onLoad(Bundle savedInstanceState) {
//            getView().textView.setText("SCREEN B - " + name);
//        }
//
//        public void click() {
//            Navigator.get(getView()).push(new ScreenBSubPageScreen("Page TWO!"));
//        }
//    }
//}
