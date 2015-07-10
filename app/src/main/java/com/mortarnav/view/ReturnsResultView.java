package com.mortarnav.view;

import android.content.Context;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.presenter.ReturnsResultPresenter;
import com.mortarnav.presenter.stackable.ReturnsResultStackableComponent;

import architect.commons.view.PresentedLinearLayout;
import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(ReturnsResultPresenter.class)
public class ReturnsResultView extends PresentedLinearLayout<ReturnsResultPresenter> {

    public ReturnsResultView(Context context) {
        super(context);

        DaggerService.<ReturnsResultStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.view_returns_result, this);
        ButterKnife.bind(view);
    }

    @OnClick(R.id.returns_result_one)
    void clickOne() {
        presenter.clickOne();
    }

    @OnClick(R.id.returns_result_two)
    void clickTwo() {
        presenter.clickTwo();
    }
}
