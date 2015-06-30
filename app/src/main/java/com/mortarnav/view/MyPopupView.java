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
}