package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.nav.SlidePageScope;
import com.mortarnav.presenter.SlidePagePresenter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mortarnav.commons.view.MvpLinearLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SlidePageView extends MvpLinearLayout<SlidePagePresenter> {

    @InjectView(R.id.page_title)
    public TextView textView;

    public SlidePageView(Context context) {
        super(context);

        DaggerService.<SlidePageScope.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.slide_page_view, this);
        ButterKnife.inject(view);
    }
}