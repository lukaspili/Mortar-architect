package com.mortarnav.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mortarnav.R;
import com.mortarnav.presenter.SlidePagePresenter;
import com.mortarnav.presenter.stackable.SlidePageStackableComponent;

import architect.commons.view.PresentedLinearLayout;
import architect.robot.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(SlidePagePresenter.class)
public class SlidePageView extends PresentedLinearLayout<SlidePagePresenter> {

    @Bind(R.id.page_title)
    public TextView textView;

    public SlidePageView(Context context) {
        super(context);

        DaggerService.<SlidePageStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.slide_page_view, this);
        ButterKnife.bind(view);
    }
}