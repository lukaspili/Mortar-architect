package com.mortarnav.mvp.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.mortarnav.R;

import architect.Screen;
import architect.commons.ScreenService;
import architect.commons.view.ScreenLinearLayout;
import architect.commons.view.ScreenViewContainer;
import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(BannerPresenter.class)
public class BannerView extends ScreenLinearLayout<BannerPresenter> implements ScreenViewContainer {

    public BannerView(Context parentContext, AttributeSet attrs) {
        super(parentContext, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        super.init(context, attrs, defStyleAttr);
    }

    @Override
    public void initWithScreenContext(Context context) {
        DaggerService.<BannerScreenComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.banner_view, this);
        ButterKnife.bind(view);
    }

    @Override
    public Screen getScreen() {
        return ScreenService.get(getContext(), BannerScreen.class);
    }

    @OnClick
    void click() {
        presenter.click();
    }


}
