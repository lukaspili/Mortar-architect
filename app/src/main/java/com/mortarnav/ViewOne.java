package com.mortarnav;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import mortarnav.library.screen.ScreenContextFactory;
import mortarnav.library.screen.ScreenUtil;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ViewOne extends LinearLayout {

    @Inject
    protected SubscreenOne.Presenter presenter;

    protected ScreenContextFactory contextFactory = new ScreenContextFactory();

    public ViewOne(Context context, AttributeSet attrs) {
        super(context, attrs);

        Context newContext = contextFactory.setUp(context, new SubscreenOne());
        ScreenUtil.<SubscreenOne.Component>getService(newContext, DaggerService.SERVICE_NAME).inject(this);

        View view = View.inflate(newContext, R.layout.subscreen_one, this);
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
