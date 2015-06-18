package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.nav.SubnavPageScope;
import com.mortarnav.presenter.SubnavPagePresenter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortarnav.commons.view.MvpFrameLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SubnavPageView extends MvpFrameLayout<SubnavPagePresenter> {

    @InjectView(R.id.subnav_page_title)
    public TextView textView;

    public SubnavPageView(Context context) {
        super(context);

        DaggerService.<SubnavPageScope.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.subnav_page_view, this);
        ButterKnife.inject(view);
    }

    @OnClick
    void onClick() {
        presenter.next();
    }
}
