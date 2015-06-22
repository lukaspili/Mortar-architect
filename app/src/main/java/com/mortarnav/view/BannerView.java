package com.mortarnav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.nav.BannerScope;
import com.mortarnav.nav.BannerScopeComponent;
import com.mortarnav.presenter.BannerPresenter;

import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mortarnav.NavigationScope;
import mortarnav.autoscope.DaggerService;
import mortarnav.commons.view.StackLinearLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(BannerScope.class)
public class BannerView extends StackLinearLayout<BannerPresenter> {

    public BannerView(Context parentContext, AttributeSet attrs) {
        super(parentContext, attrs);
    }

    @Override
    public NavigationScope getScope() {
        return new BannerScope();
    }

    @Override
    public void initWithContext(Context context) {
        DaggerService.<BannerScopeComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.banner_view, this);
        ButterKnife.inject(view);
    }

    @OnClick
    void click() {
        presenter.click();
    }
}
