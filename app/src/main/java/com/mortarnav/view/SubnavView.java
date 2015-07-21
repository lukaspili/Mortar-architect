package com.mortarnav.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;

import com.mortarnav.R;
import com.mortarnav.ToolbarOwner;
import com.mortarnav.presenter.SubnavPresenter;
import com.mortarnav.presenter.stackable.SubnavStackableComponent;

import javax.inject.Inject;

import architect.NavigatorView;
import architect.commons.view.PresentedFrameLayout;
import architect.robot.DaggerService;
import architect.view.HandlesBack;
import architect.view.HandlesViewTransition;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(SubnavPresenter.class)
public class SubnavView extends PresentedFrameLayout<SubnavPresenter> implements HandlesBack, HandlesViewTransition {

    @Inject
    protected ToolbarOwner toolbarOwner;

    @Bind(R.id.sub_navigator)
    public NavigatorView navigatorView;

    public SubnavView(Context context) {
        super(context);

        DaggerService.<SubnavStackableComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.subnav_view, this);
        ButterKnife.bind(view);
    }

    @Override
    public boolean onBackPressed() {
        return presenter.backPressed();
    }


    @Override
    public void onViewTransition(AnimatorSet set) {
        if (set != null) {
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    toolbarOwner.setTitle("Subnav presenter!");
                }
            });
        } else {
            toolbarOwner.setTitle("Subnav presenter!");
        }
    }
}
