package com.mortarnav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.nav.BannerScope;
import com.mortarnav.presenter.BannerPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mortarnav.library.NavigationScopeFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class BannerView extends LinearLayout {

    @Inject
    protected BannerPresenter presenter;

    public BannerView(Context parentContext, AttributeSet attrs) {
        super(parentContext, attrs);

        Context context = NavigationScopeFactory.createContext(parentContext, new BannerScope());

        DaggerService.<BannerScope.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.banner_view, this);
        ButterKnife.inject(view);
    }

    @OnClick
    void click() {
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
