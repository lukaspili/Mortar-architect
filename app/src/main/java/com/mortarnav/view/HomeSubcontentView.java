package com.mortarnav.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mortarnav.DaggerService;
import com.mortarnav.R;
import com.mortarnav.nav.HomeSubcontentScope;
import com.mortarnav.presenter.HomeSubcontentPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortarnav.library.NavigationScopeFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeSubcontentView extends LinearLayout {

    @Inject
    protected HomeSubcontentPresenter presenter;

    @InjectView(R.id.home_sub_random)
    public TextView randomTextView;

    public HomeSubcontentView(Context parentContext, AttributeSet attrs) {
        super(parentContext, attrs);

        Context context = NavigationScopeFactory.createContext(parentContext, new HomeSubcontentScope());

        DaggerService.<HomeSubcontentScope.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.home_subcontent_view, this);
        ButterKnife.inject(view);
    }

    @OnClick
    void click() {
        presenter.click();
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
