package com.mortarnav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.presenter.HomeDoubleSubcontentPresenter;
import com.mortarnav.presenter.scope.HomeDoubleSubcontentScope;
import com.mortarnav.presenter.scope.HomeDoubleSubcontentScopeComponent;

import architect.StackScope;
import architect.autostack.DaggerService;
import architect.commons.view.StackedLinearLayout;
import autodagger.AutoInjector;
import butterknife.ButterKnife;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(HomeDoubleSubcontentPresenter.class)
public class HomeDoubleSubcontentView extends StackedLinearLayout<HomeDoubleSubcontentPresenter> {


    public HomeDoubleSubcontentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public StackScope getScope() {
        return new HomeDoubleSubcontentScope();
    }

    @Override
    public void initWithContext(Context context) {
        DaggerService.<HomeDoubleSubcontentScopeComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.home_double_subcontent_view, this);
        ButterKnife.inject(view);
    }
}
