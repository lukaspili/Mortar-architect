//package com.mortarnav.presenter;
//
//import android.os.Bundle;
//
//import com.mortarnav.DaggerScope;
//import com.mortarnav.MainActivity;
//import com.mortarnav.Parceler;
//import com.mortarnav.presenter.stackable.SubnavPageStackable;
//import com.mortarnav.view.SubnavView;
//
//import javax.inject.Inject;
//
//import architect.Navigator;
//import architect.TransitionsMapping;
//import architect.commons.transition.LateralViewTransition;
//import architect.robot.AutoStackable;
//import autodagger.AutoComponent;
//import mortar.MortarScope;
//import mortar.ViewPresenter;
//import timber.log.Timber;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//@AutoStackable(
//        component = @AutoComponent(dependencies = MainActivity.class),
//        pathWithView = SubnavView.class
//)
//@DaggerScope(SubnavPresenter.class)
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
//            navigator = Navigator.create(MortarScope.getScope(getView().getContext()), new Parceler());
//            navigator.transitions().register(new TransitionsMapping().byDefault(new LateralViewTransition()));
//        }
//
//        navigator.delegate().onCreate(null, savedInstanceState, getView().navigatorView, new SubnavPageStackable("INITIAL"));
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
