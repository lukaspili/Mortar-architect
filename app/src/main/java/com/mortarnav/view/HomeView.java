package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mortarnav.R;
import com.mortarnav.presenter.HomePresenter;
import com.mortarnav.stackable.HomeStackable;

import architect.commons.view.PresentedScrollView;
import architect.robot.DaggerService;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeView extends PresentedScrollView<HomePresenter> {

    @Bind(R.id.home_title)
    public TextView titleTextView;

    @Bind(R.id.home_subtitle)
    public TextView subtitleTextView;

    public HomeView(Context context) {
        super(context);

        DaggerService.<HomeStackable.Component>get(context).inject(this);

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
}
