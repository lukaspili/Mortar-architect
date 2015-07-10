//package architect.commons;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//
//import architect.Navigator;
//import architect.NavigatorView;
//import architect.Path;
//import architect.TransitionsMapping;
//import mortar.MortarScope;
//import mortar.bundler.BundleServiceRunner;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public abstract class Architect2Activity extends Activity {
//
//    protected MortarScope scope;
//    protected Navigator navigator;
//
//    protected abstract void configureScope(MortarScope.Builder builder, MortarScope parMortarScope);
//
//    protected abstract Navigator createNavigator(MortarScope scope);
//
//
//    @Override
//    public Object getSystemService(String name) {
//        if (scope != null && scope.hasService(name)) {
//            return scope.getService(name);
//        }
//
//        return super.getSystemService(name);
//    }
//
//    protected void onCreateScope(Bundle savedInstanceState) {
//        String scopeName = getLocalClassName() + "-task-" + getTaskId();
//        scope = MortarScope.findChild(getApplicationContext(), scopeName);
//        if (scope == null) {
//            MortarScope parentScope = MortarScope.getScope(getApplicationContext());
//
//            MortarScope.Builder builder = parentScope.buildChild()
//                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner());
//            configureScope(builder, parentScope);
//            scope = builder.build(scopeName);
//
//            createNavigator(scope);
//
////            Navigator navigator = Navigator.create(scope, getNavigatorConfig());
////            navigator.transitions().register(getTransitionsMapping());
//        }
//
//        BundleServiceRunner.getBundleServiceRunner(scope).onCreate(savedInstanceState);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        onCreateScope(savedInstanceState);
//
////        createContentView();
////
////        navigator = Navigator.find(this);
////        navigator.delegate().onCreate(getIntent(), savedInstanceState, getNavigatorView(), getInitialPath());
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        navigator.delegate().onNewIntent(intent);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        BundleServiceRunner.getBundleServiceRunner(scope).onSaveInstanceState(outState);
//        navigator.delegate().onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        navigator.delegate().onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        navigator.delegate().onStop();
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        navigator.delegate().onDestroy();
//        navigator = null;
//
//        if (isFinishing() && scope != null) {
//            scope.destroy();
//            scope = null;
//        }
//
//        super.onDestroy();
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (navigator.delegate().onBackPressed()) {
//            return;
//        }
//
//        super.onBackPressed();
//    }
//}
