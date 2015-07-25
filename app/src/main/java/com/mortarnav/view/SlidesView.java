//package com.mortarnav.view;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.content.Context;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//
//import com.mortarnav.R;
//import com.mortarnav.ToolbarOwner;
//import com.mortarnav.presenter.SlidesPresenter;
//import com.mortarnav.presenter.stackable.SlidePageStackable;
//import com.mortarnav.presenter.stackable.SlidesStackableComponent;
//
//import javax.inject.Inject;
//
//import architect.ViewTransitionDirection;
//import architect.commons.adapter.StackablePagerAdapter;
//import architect.commons.view.PresentedLinearLayout;
//import architect.robot.DaggerService;
//import architect.view.HandlesViewTransition;
//import autodagger.AutoInjector;
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//@AutoInjector(SlidesPresenter.class)
//public class SlidesView extends PresentedLinearLayout<SlidesPresenter> implements HandlesViewTransition {
//
//    @Inject
//    protected ToolbarOwner toolbarOwner;
//
//    @Bind(R.id.pager)
//    public ViewPager viewPager;
//
//    public StackablePagerAdapter adapter;
//
//    public SlidesView(Context context) {
//        super(context);
//
//        DaggerService.<SlidesStackableComponent>get(context).inject(this);
//
//        View view = View.inflate(context, R.layout.slides_view, this);
//        ButterKnife.bind(view);
//    }
//
//    public void show(SlidePageStackable[] pages) {
//        adapter = new StackablePagerAdapter(getContext(), pages);
//        viewPager.setAdapter(adapter);
//    }
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