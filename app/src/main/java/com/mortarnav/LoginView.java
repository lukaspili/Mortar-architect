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
public class LoginView extends LinearLayout {

    @Inject
    protected LoginScreen.Presenter presenter;

    public LoginView(Context context) {
        super(context);

        NavigatorServices.<LoginScreen.Component>getService(context, DaggerService.SERVICE_NAME).inject(this);

        View view = View.inflate(context, R.layout.screen_login, this);
        ButterKnife.inject(view);
    }

    @OnClick(R.id.login_button)
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
