package com.mortarnav.view;

import android.content.Context;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.presenter.MyPopupPresenter;
import com.mortarnav.presenter.scope.MyPopupScopeComponent;

import architect.autostack.DaggerService;
import architect.commons.view.PresenterLinearLayout;
import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(MyPopupPresenter.class)
public class MyPopupView extends PresenterLinearLayout<MyPopupPresenter> {

    public MyPopupView(Context context) {
        super(context);

        DaggerService.<MyPopupScopeComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.my_popup_view, this);
        ButterKnife.inject(view);
    }

    @OnClick(R.id.my_popup_show_home_button)
    void homeClick() {
        presenter.homeClick();
    }

    @OnClick(R.id.my_popup_show_popup_button)
    void popupClick() {
        presenter.popupClick();
    }

}