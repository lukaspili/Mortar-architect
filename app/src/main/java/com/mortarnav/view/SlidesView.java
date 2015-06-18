package com.mortarnav.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.nav.SlidesScope;
import com.mortarnav.nav.path.SlidePagePath;
import com.mortarnav.presenter.SlidesPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mortarnav.commons.adapter.PathPagerAdapter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SlidesView extends LinearLayout {

    @Inject
    protected SlidesPresenter presenter;

    @InjectView(R.id.pager)
    public ViewPager viewPager;

    public PathPagerAdapter adapter;

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

    public void show(SlidePagePath[] pagePaths) {
        adapter = new PathPagerAdapter(getContext(), pagePaths);
        viewPager.setAdapter(adapter);
    }
}
