package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.nav.SlidesScope;
import com.mortarnav.presenter.SlidesPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SlidesView extends LinearLayout {

    @Inject
    protected SlidesPresenter presenter;

    public SlidesView(Context context) {
        super(context);

        DaggerService.<SlidesScope.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.slides_view, this);
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
