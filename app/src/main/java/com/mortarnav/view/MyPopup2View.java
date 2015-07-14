package com.mortarnav.view;

import android.content.Context;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.presenter.MyPopup2Presenter;
import com.mortarnav.presenter.stackable.MyPopup2StackableComponent;

import architect.commons.view.PresentedLinearLayout;
import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(MyPopup2Presenter.class)
public class MyPopup2View extends PresentedLinearLayout<MyPopup2Presenter> {

    public MyPopup2View(Context context) {
        super(context);

        DaggerService.<MyPopup2StackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.my_popup2_view, this);
        ButterKnife.bind(view);
    }

    @OnClick(R.id.my_popup2_show_popup_button)
    void popupClick() {
        presenter.popupClick();
    }

    @OnClick(R.id.my_popup2_dismiss)
    void dismissClick() {
        presenter.dismissClick();
    }
}