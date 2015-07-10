package com.mortarnav.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.presenter.SlidesPresenter;
import com.mortarnav.presenter.stackable.SlidePageStackable;
import com.mortarnav.presenter.stackable.SlidesStackableComponent;

import architect.commons.adapter.StackablePagerAdapter;
import architect.commons.view.PresentedLinearLayout;
import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(SlidesPresenter.class)
public class SlidesView extends PresentedLinearLayout<SlidesPresenter> {

    @Bind(R.id.pager)
    public ViewPager viewPager;

    public StackablePagerAdapter adapter;

    public SlidesView(Context context) {
        super(context);

        DaggerService.<SlidesStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.slides_view, this);
        ButterKnife.bind(view);
    }

    public void show(SlidePageStackable[] pages) {
        adapter = new StackablePagerAdapter(getContext(), pages);
        viewPager.setAdapter(adapter);
    }
}