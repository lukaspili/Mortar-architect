package com.mortarnav.view;

import android.content.Context;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.presenter.SubnavPresenter;
import com.mortarnav.presenter.scope.SubnavScopeComponent;

import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.InjectView;
import mortarnav.NavigatorView;
import mortarnav.autoscope.DaggerService;
import mortarnav.commons.view.MvpFrameLayout;
import mortarnav.view.HandlesBack;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(SubnavPresenter.class)
public class SubnavView extends MvpFrameLayout<SubnavPresenter> implements HandlesBack {

    @InjectView(R.id.sub_navigator)
    public NavigatorView navigatorView;

    public SubnavView(Context context) {
        super(context);

        DaggerService.<SubnavScopeComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.subnav_view, this);
        ButterKnife.inject(view);
    }

    @Override
    public boolean onBackPressed() {
        return presenter.backPressed();
    }
}
