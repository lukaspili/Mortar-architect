package com.mortarnav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.presenter.BannerPresenter;
import com.mortarnav.stack.BannerStackScope;
import com.mortarnav.stack.BannerStackScopeComponent;

import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.OnClick;
import architect.StackScope;
import architect.autostack.DaggerService;
import architect.commons.view.StackLinearLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(BannerStackScope.class)
public class BannerView extends StackLinearLayout<BannerPresenter> {

    public BannerView(Context parentContext, AttributeSet attrs) {
        super(parentContext, attrs);
    }

    @Override
    public StackScope getScope() {
        return new BannerStackScope();
    }

    @Override
    public void initWithContext(Context context) {
        DaggerService.<BannerStackScopeComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.banner_view, this);
        ButterKnife.inject(view);
    }

    @OnClick
    void click() {
        presenter.click();
    }
}
