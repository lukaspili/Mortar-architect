package com.mortarnav.mvp.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.mortarnav.DaggerService;
import com.mortarnav.R;

import architect.MortarFactory;
import architect.commons.SubscreenService;
import architect.commons.view.PresentedLinearLayout;
import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(BannerPresenter.class)
public class BannerView extends PresentedLinearLayout<BannerPresenter> {

    public BannerView(Context parentContext, AttributeSet attrs) {
        super(parentContext, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        Context screenContext = MortarFactory.createContext(context, SubscreenService.get(context, "bannerScreen"));

        DaggerService.<BannerScreenComponent>get(screenContext).inject(this);
        View view = View.inflate(screenContext, R.layout.banner_view, this);
        ButterKnife.bind(view);
    }

    @OnClick
    void click() {
        presenter.click();
    }


}
