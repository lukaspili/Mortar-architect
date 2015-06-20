//package com.mortarnav.presenter;
//
//import android.os.Bundle;
//
//import com.mortarnav.DaggerScope;
//import com.mortarnav.nav.SubnavScope;
//import com.mortarnav.nav.path.SubnavPagePath;
//import com.mortarnav.nav.path.SubnavPath;
//import com.mortarnav.path.HomePath;
//import com.mortarnav.view.SubnavView;
//
//import javax.inject.Inject;
//
//import mortar.MortarScope;
//import mortar.ViewPresenter;
//import mortarnav.Navigator;
//import mortarnav.Transition;
//import mortarnav.transition.HorizontalScreenTransition;
//import timber.log.Timber;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//@DaggerScope(SubnavScope.Component.class)
//public class SubnavPresenter extends ViewPresenter<SubnavView> {
//
//    private Navigator navigator;
//
//    @Inject
//    public SubnavPresenter() {
//    }
//
//    @Override
//    protected void onLoad(Bundle savedInstanceState) {
//        navigator = Navigator.find(getView().getContext());
//        if (navigator == null) {
//            Timber.d("create navigator");
//            navigator = Navigator.create(MortarScope.getScope(getView().getContext()));
//            navigator.transitions().register(Transition.defaultTransition(new HorizontalScreenTransition()));
//        }
//
//        navigator.delegate().onCreate(null, savedInstanceState, getView().navigatorView, new SubnavPagePath("INITIAL"));
//        navigator.delegate().onStart();
//    }
//
//    @Override
//    protected void onSave(Bundle outState) {
//        navigator.delegate().onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void dropView(SubnavView view) {
//        navigator.delegate().onStop();
//        navigator.delegate().onDestroy();
//        navigator = null;
//
//        super.dropView(view);
//    }
//
//    public boolean backPressed() {
//        return navigator.delegate().onBackPressed();
//    }
//}
