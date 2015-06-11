package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.screen.LoginScreen;
import com.mortarnav.screen.ScreenBSubPageScreen;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortarnav.library.NavigatorServices;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenBSubPageView extends FrameLayout {

    @Inject
    protected ScreenBSubPageScreen.Presenter presenter;

    @InjectView(R.id.screen_b_sub_title)
    public TextView textView;

    public ScreenBSubPageView(Context context) {
        super(context);

        DaggerService.<ScreenBSubPageScreen.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.screen_b_sub_page, this);
        ButterKnife.inject(view);
    }

    @OnClick()
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
