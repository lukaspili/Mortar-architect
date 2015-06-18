package com.mortarnav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.nav.BannerScope;
import com.mortarnav.presenter.BannerPresenter;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mortarnav.NavigationScope;
import mortarnav.commons.view.MvpContainerLinearLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class BannerView extends MvpContainerLinearLayout<BannerPresenter> {

    public BannerView(Context parentContext, AttributeSet attrs) {
        super(parentContext, attrs);
    }

    @Override
    public NavigationScope getScope() {
        return new BannerScope();
    }

    @Override
    public void initWithContext(Context context) {
        DaggerService.<BannerScope.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.banner_view, this);
        ButterKnife.inject(view);
    }

    @OnClick
    void click() {
        presenter.click();
    }
}
