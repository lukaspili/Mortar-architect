package com.mortarnav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.mortarnav.R;
import com.mortarnav.nav.HomeScope;
import com.mortarnav.presenter.HomePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mortarnav.autoscope.DaggerService;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeAdditionalCustomView extends FrameLayout {

    @Inject
    protected HomePresenter presenter;

    public HomeAdditionalCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);

        DaggerService.<HomeScope.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.home_additional_custom_view, this);
        ButterKnife.inject(view);
    }

    @OnClick
    void onClick() {
        presenter.customViewClick();
    }


}
