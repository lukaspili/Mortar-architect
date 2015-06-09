package com.mortarnav;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ViewB extends LinearLayout {

    @Inject
    protected ScreenB.Presenter presenter;

    @InjectView(R.id.name)
    public TextView nameTextView;

    public ViewB(Context context) {
        super(context);

        ((ScreenB.Component) context.getSystemService(DaggerService.SERVICE_NAME)).inject(this);

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
}
