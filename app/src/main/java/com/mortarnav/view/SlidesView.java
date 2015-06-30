package com.mortarnav.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.presenter.SlidesPresenter;
import com.mortarnav.presenter.scope.SlidesScopeComponent;
import com.mortarnav.presenter.scope.path.SlidePagePath;

import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.InjectView;
import architect.autostack.DaggerService;
import architect.commons.adapter.StackPagerAdapter;
import architect.commons.view.PresenterLinearLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(SlidesPresenter.class)
public class SlidesView extends PresenterLinearLayout<SlidesPresenter> {

    @InjectView(R.id.pager)
    public ViewPager viewPager;

    public StackPagerAdapter adapter;

    public SlidesView(Context context) {
        super(context);

        DaggerService.<SlidesScopeComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.slides_view, this);
        ButterKnife.inject(view);
    }

    public void show(SlidePagePath[] pagePaths) {
        adapter = new StackPagerAdapter(getContext(), pagePaths);
        viewPager.setAdapter(adapter);
    }
}