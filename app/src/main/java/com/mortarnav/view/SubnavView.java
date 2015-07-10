package com.mortarnav.view;

import android.content.Context;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.presenter.SubnavPresenter;
import com.mortarnav.presenter.stackable.SubnavStackableComponent;

import architect.NavigatorView;
import architect.commons.view.PresentedFrameLayout;
import architect.robot.DaggerService;
import architect.view.HandlesBack;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(SubnavPresenter.class)
public class SubnavView extends PresentedFrameLayout<SubnavPresenter> implements HandlesBack {

    @Bind(R.id.sub_navigator)
    public NavigatorView navigatorView;

    public SubnavView(Context context) {
        super(context);

        DaggerService.<SubnavStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.subnav_view, this);
        ButterKnife.bind(view);
    }

    @Override
    public boolean onBackPressed() {
        return presenter.backPressed();
    }
}
