package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.nav.SlidePageScope;
import com.mortarnav.presenter.SlidePagePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SlidePageView extends LinearLayout {

    @Inject
    protected SlidePagePresenter presenter;

    @InjectView(R.id.page_title)
    public TextView textView;

    public SlidePageView(Context context) {
        super(context);

        DaggerService.<SlidePageScope.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.slide_page_view, this);
        ButterKnife.inject(view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }
}
