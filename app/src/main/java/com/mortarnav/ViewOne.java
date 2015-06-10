package com.mortarnav;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import mortarnav.library.NavigatorServices;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ViewOne extends LinearLayout {

    @Inject
    protected SubscreenOne.Presenter presenter;

    public ViewOne(Context context) {
        super(context);

        NavigatorServices.<SubscreenOne.Component>getService(context, DaggerService.SERVICE_NAME).inject(this);
        View view = View.inflate(context, R.layout.subscreen_one, this);
        ButterKnife.inject(view);
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
