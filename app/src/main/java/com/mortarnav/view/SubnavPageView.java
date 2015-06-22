package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mortarnav.R;
import com.mortarnav.presenter.SubnavPagePresenter;
import com.mortarnav.presenter.scope.SubnavPageScopeComponent;

import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortarnav.autoscope.DaggerService;
import mortarnav.commons.view.MvpFrameLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(SubnavPagePresenter.class)
public class SubnavPageView extends MvpFrameLayout<SubnavPagePresenter> {

    @InjectView(R.id.subnav_page_title)
    public TextView textView;

    public SubnavPageView(Context context) {
        super(context);

        DaggerService.<SubnavPageScopeComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.subnav_page_view, this);
        ButterKnife.inject(view);
    }

    @OnClick
    void onClick() {
        presenter.next();
    }
}
