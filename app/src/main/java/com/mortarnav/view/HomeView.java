package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.nav.HomeScope;
import com.mortarnav.presenter.HomePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeView extends LinearLayout {

    @Inject
    protected HomePresenter presenter;

    @InjectView(R.id.home_title)
    public TextView titleTextView;

    @InjectView(R.id.home_subtitle)
    public TextView subtitleTextView;

    public HomeView(Context context) {
        super(context);

        DaggerService.<HomeScope.Component>get(context).inject(this);

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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }
}
