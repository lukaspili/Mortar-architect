package com.mortarnav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.presenter.BannerPresenter;
import com.mortarnav.stackable.BannerStackable;
import com.mortarnav.stackable.BannerStackableComponent;

import architect.Stackable;
import architect.commons.view.StackedLinearLayout;
import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(BannerPresenter.class)
public class BannerView extends StackedLinearLayout<BannerPresenter> {

    public BannerView(Context parentContext, AttributeSet attrs) {
        super(parentContext, attrs);
    }

    @Override
    public Stackable getStackable() {
        return new BannerStackable();
    }

    @Override
    public void initWithContext(Context context) {
        DaggerService.<BannerStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.banner_view, this);
        ButterKnife.bind(view);
    }

    @OnClick
    void click() {
        presenter.click();
    }
}
