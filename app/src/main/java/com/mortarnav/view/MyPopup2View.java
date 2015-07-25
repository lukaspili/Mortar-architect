//package com.mortarnav.view;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.content.Context;
//import android.view.View;
//
//import com.mortarnav.R;
//import com.mortarnav.ToolbarOwner;
//import com.mortarnav.presenter.MyPopup2Presenter;
//import com.mortarnav.presenter.stackable.MyPopup2StackableComponent;
//
//import javax.inject.Inject;
//
//import architect.commons.view.PresentedLinearLayout;
//import architect.robot.DaggerService;
//import architect.view.HandlesViewTransition;
//import autodagger.AutoInjector;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//@AutoInjector(MyPopup2Presenter.class)
//public class MyPopup2View extends PresentedLinearLayout<MyPopup2Presenter> implements HandlesViewTransition {
//
//    @Inject
//    protected ToolbarOwner toolbarOwner;
//
//    public MyPopup2View(Context context) {
//        super(context);
//
//        DaggerService.<MyPopup2StackableComponent>get(context).inject(this);
//
//        View view = View.inflate(context, R.layout.my_popup2_view, this);
//        ButterKnife.bind(view);
//    }
//
//    @OnClick(R.id.my_popup2_show_popup_button)
//    void popupClick() {
//        presenter.popupClick();
//    }
//
//    @OnClick(R.id.my_popup2_dismiss)
//    void dismissClick() {
//        presenter.dismissClick();
//    }
//
//
//    @Override
//    public void onViewTransition(AnimatorSet set) {
//        if (set != null) {
//            set.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    toolbarOwner.hide();
//                }
//            });
//            Animator animator = toolbarOwner.animateHide();
//            if (animator != null) {
//                set.play(animator);
//            }
//        } else {
//            toolbarOwner.hide();
//        }
//    }
//}