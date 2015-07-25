package com.mortarnav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.presenter.BannerPresenter;
import com.mortarnav.stackable.BannerScreen;
import com.mortarnav.stackable.BannerScreenComponent;

import architect.Screen;
import architect.commons.view.ScreenLinearLayout;
import architect.commons.view.ScreenViewContainer;
import architect.robot.DaggerService;
import architect.commons.ScreenService;
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
    public void initWithScreenContext(Context context) {
        DaggerService.<BannerScreenComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.banner_view, this);
        ButterKnife.bind(view);
    }

    @Override
    public Screen getScreen() {
        return ScreenService.<BannerScreen.StateWithBannerScreen>get(getContext()).getBannerScreen();
    }

    @OnClick
    void click() {
        presenter.click();
    }


}
