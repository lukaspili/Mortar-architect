package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mortarnav.R;
import com.mortarnav.stack.HomeStackScope;
import com.mortarnav.presenter.HomePresenter;

import architect.commons.view.PresentedScrollView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import architect.autostack.DaggerService;
import architect.commons.view.PresenterLinearLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeView extends PresentedScrollView<HomePresenter> {

    @InjectView(R.id.home_title)
    public TextView titleTextView;

    @InjectView(R.id.home_subtitle)
    public TextView subtitleTextView;

    public HomeView(Context context) {
        super(context);

        DaggerService.<HomeStackScope.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.home_view, this);
        ButterKnife.inject(view);
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
}
