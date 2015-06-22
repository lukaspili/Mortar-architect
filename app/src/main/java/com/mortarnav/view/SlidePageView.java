package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mortarnav.R;
import com.mortarnav.presenter.SlidePagePresenter;
import com.mortarnav.presenter.scope.SlidePageScopeComponent;

import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.InjectView;
import mortarnav.autoscope.DaggerService;
import mortarnav.commons.view.MvpLinearLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(SlidePagePresenter.class)
public class SlidePageView extends MvpLinearLayout<SlidePagePresenter> {

    @InjectView(R.id.page_title)
    public TextView textView;

    public SlidePageView(Context context) {
        super(context);

        DaggerService.<SlidePageScopeComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.slide_page_view, this);
        ButterKnife.inject(view);
    }
}