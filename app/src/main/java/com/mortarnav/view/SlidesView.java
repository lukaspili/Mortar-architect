package com.mortarnav.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.nav.SlidesScope;
import com.mortarnav.nav.path.SlidePagePath;
import com.mortarnav.presenter.SlidesPresenter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mortarnav.commons.adapter.PathPagerAdapter;
import mortarnav.commons.view.MvpLinearLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SlidesView extends MvpLinearLayout<SlidesPresenter> {

    @InjectView(R.id.pager)
    public ViewPager viewPager;

    public PathPagerAdapter adapter;

    public SlidesView(Context context) {
        super(context);

        DaggerService.<SlidesScope.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.slides_view, this);
        ButterKnife.inject(view);
    }

    public void show(SlidePagePath[] pagePaths) {
        adapter = new PathPagerAdapter(getContext(), pagePaths);
        viewPager.setAdapter(adapter);
    }
}