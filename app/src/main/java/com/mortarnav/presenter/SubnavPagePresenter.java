//package com.mortarnav.presenter;
//
//import android.os.Bundle;
//
//import com.mortarnav.DaggerScope;
//import com.mortarnav.nav.SubnavPageScope;
//import com.mortarnav.nav.path.SubnavPagePath;
//import com.mortarnav.view.SubnavPageView;
//
//import java.util.Random;
//
//import mortar.ViewPresenter;
//import mortarnav.Navigator;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//@DaggerScope(SubnavPageScope.Component.class)
//public class SubnavPagePresenter extends ViewPresenter<SubnavPageView> {
//
//    private final String title;
//
//    public SubnavPagePresenter(String title) {
//        this.title = title;
//    }
//
//    @Override
//    protected void onLoad(Bundle savedInstanceState) {
//        getView().textView.setText("Subnav page view: " + title);
//    }
//
//    public void next() {
//        Navigator.get(getView()).push(new SubnavPagePath("random " + new Random().nextInt(100)));
//    }
//}
