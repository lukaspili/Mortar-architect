package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.screen.ScreenB;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortarnav.library.NavigatorContainerView;
import mortarnav.library.view.HandlesBack;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ViewB extends LinearLayout implements HandlesBack {

    @Inject
    protected ScreenB.Presenter presenter;

    @InjectView(R.id.name)
    public TextView nameTextView;

    @InjectView(R.id.screen_b_navigatorcontainer)
    public NavigatorContainerView containerView;

    public ViewB(Context context) {
        super(context);

        DaggerService.<ScreenB.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_b, this);
        ButterKnife.inject(view);
    }

    @OnClick(R.id.button)
    void buttonClick() {
        presenter.click();
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

    public void configure(String name) {
        nameTextView.setText(name);
    }

    @Override
    public boolean onBackPressed() {
        return presenter.backPressed();
    }
}
