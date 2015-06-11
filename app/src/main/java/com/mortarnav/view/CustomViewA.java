package com.mortarnav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.screen.ScreenA;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mortarnav.library.NavigatorServices;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class CustomViewA extends FrameLayout {

    @Inject
    protected ScreenA.Presenter presenter;

    public CustomViewA(Context context, AttributeSet attrs) {
        super(context, attrs);

        NavigatorServices.<ScreenA.Component>getService(context, DaggerService.SERVICE_NAME).inject(this);

        View view = View.inflate(context, R.layout.customview_a, this);
        ButterKnife.inject(view);
    }

    @OnClick
    void onClick() {
        presenter.customViewClick();
    }


}
