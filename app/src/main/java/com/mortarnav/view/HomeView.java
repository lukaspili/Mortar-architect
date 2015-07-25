package com.mortarnav.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mortarnav.R;
import com.mortarnav.ToolbarOwner;
import com.mortarnav.presenter.HomePresenter;
import com.mortarnav.stackable.HomeScreen;

import javax.inject.Inject;

import architect.commons.view.PresentedScrollView;
import architect.robot.DaggerService;
import architect.view.HandlesViewTransition;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeView extends PresentedScrollView<HomePresenter> implements HandlesViewTransition {

    @Inject
    protected ToolbarOwner toolbarOwner;

    @Bind(R.id.home_title)
    public TextView titleTextView;

    @Bind(R.id.home_subtitle)
    public TextView subtitleTextView;

    public HomeView(Context context) {
        super(context);

        DaggerService.<HomeScreen.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.home_view, this);
        ButterKnife.bind(view);
    }

    @OnClick(R.id.next_home_button)
    void nextHomeClick() {
        presenter.nextHomeClick();
    }


    @OnClick(R.id.pager_button)
    void pagerClick() {
        presenter.pagerClick();
    }

    @OnClick(R.id.subnav_button)
    void subnavClick() {
        presenter.subnavClick();
    }

    @OnClick(R.id.show_popup)
    void showPopupClick() {
        presenter.showPopupClick();
    }

    @OnClick(R.id.replace_new_home)
    void replaceNewHomeClick() {
        presenter.replaceNewHomeClick();
    }

    @OnClick(R.id.show_returns_result)
    void showReturnsResultClick() {
        presenter.showReturnsResultClick();
    }

    @OnClick(R.id.back_root)
    void backRootClick() {
        presenter.backToRootClick();
    }

    @OnClick(R.id.home_show_popup_two)
    void showPopupTwoClick() {
        presenter.showPopupTwoClick();
    }

    @OnClick(R.id.home_show_two_popups)
    void showTwoPopupsClick() {
        presenter.showTwoPopupsClick();
    }

    @OnClick(R.id.home_set_new_stack)
    void setNewStackClick() {
        presenter.setNewStackClick();
    }


    @Override
    public void onViewTransition(AnimatorSet set) {
        if (set != null) {
            set.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    toolbarOwner.show();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    toolbarOwner.setTitle("Hello Home!");
                }
            });
            Animator animator = toolbarOwner.animateShow();
            Timber.d("Animate show: %s", animator);
            if (animator != null) {
                set.play(animator);
            }
        } else {
            toolbarOwner.show();
            toolbarOwner.setTitle("Hello Home!");
        }
    }
}
