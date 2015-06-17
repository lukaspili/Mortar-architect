//package com.mortarnav.screen;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//
//import com.mortarnav.DaggerScope;
//import com.mortarnav.DaggerService;
//import com.mortarnav.MainActivity;
//import com.mortarnav.view.LoginView;
//
//import javax.inject.Inject;
//
//import mortar.MortarScope;
//import mortar.ViewPresenter;
//import mortarnav.library.ScreenContextFactory;
//import mortarnav.library.ScreenScope;
//import timber.log.Timber;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class LoginScreen extends ScreenScope {
//
//    @Override
//    public View createView(Context context) {
//        return new LoginView(context);
//    }
//
//    @Override
//    public void configureMortarScope(ScreenContextFactory.BuilderContext builderContext) {
//        // get main activity scope
//        // which is not the parent scope
//        MortarScope activityScope = MortarScope.findChild(builderContext.getParentContext().getApplicationContext(), MainActivity.class.getName());
//
//
//
//        builderContext.getScopeBuilder().withService(DaggerService.SERVICE_NAME, DaggerLoginScreen_Component.builder()
//                .component(activityScope.<MainActivity.Component>getService(DaggerService.SERVICE_NAME))
//                .build());
//    }
//
//    @dagger.Component(dependencies = MainActivity.Component.class)
//    @DaggerScope(Component.class)
//    public interface Component {
//
//        void inject(LoginView view);
//    }
//
//    @DaggerScope(Component.class)
//    public static class Presenter extends ViewPresenter<LoginView> {
//
//        @Inject
//        public Presenter() {
//        }
//
//        @Override
//        protected void onLoad(Bundle savedInstanceState) {
//            Timber.d("Presenter LoginView onLoad %s", this);
//        }
//
//        public void click() {
//            System.out.println("Login click");
//        }
//    }
//}
