package com.mortarnav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.mortarnav.R;
import com.mortarnav.presenter.HomeSubcontentPresenter;
import com.mortarnav.presenter.scope.HomeSubcontentScope;
import com.mortarnav.presenter.scope.HomeSubcontentScopeComponent;

import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortarnav.StackScope;
import mortarnav.autoscope.DaggerService;
import mortarnav.commons.view.StackLinearLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(HomeSubcontentPresenter.class)
public class HomeSubcontentView extends StackLinearLayout<HomeSubcontentPresenter> {

    @InjectView(R.id.home_sub_random)
    public TextView randomTextView;

    public HomeSubcontentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public StackScope getScope() {
        return new HomeSubcontentScope();
    }

    @Override
    public void initWithContext(Context context) {
        DaggerService.<HomeSubcontentScopeComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.home_subcontent_view, this);
        ButterKnife.inject(view);
    }

    @OnClick
    void click() {
        presenter.click();
    }
}
