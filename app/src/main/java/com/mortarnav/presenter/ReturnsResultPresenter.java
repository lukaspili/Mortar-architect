//package com.mortarnav.presenter;
//
//import com.mortarnav.DaggerScope;
//import com.mortarnav.StandardAutoComponent;
//import com.mortarnav.view.ReturnsResultView;
//
//import architect.Navigator;
//import architect.robot.AutoStackable;
//import autodagger.AutoComponent;
//import mortar.ViewPresenter;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//@AutoStackable(
//        component = @AutoComponent(includes = StandardAutoComponent.class),
//        pathWithView = ReturnsResultView.class
//)
//@DaggerScope(ReturnsResultPresenter.class)
//public class ReturnsResultPresenter extends ViewPresenter<ReturnsResultView> {
//
//    public void clickTwo() {
//        Navigator.get(getView()).back("Result TWO");
//    }
//
//    public void clickOne() {
//        Navigator.get(getView()).back("Result ONE");
//    }
//
//
//}
