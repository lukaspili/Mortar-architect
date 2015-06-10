package com.mortarnav;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mortarnav.library.NavigatorServices;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ViewA extends LinearLayout {

    @Inject
    protected ScreenA.Presenter presenter;

    public ViewA(Context context) {
        super(context);

        NavigatorServices.<ScreenA.Component>getService(context, DaggerService.SERVICE_NAME).inject(this);

        View view = View.inflate(context, R.layout.screen_a, this);
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
}
