package com.mortarnav;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import architect.NavigatorView;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorViewBehavior extends CoordinatorLayout.Behavior<NavigatorView> {

    public NavigatorViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, NavigatorView child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, NavigatorView child, View dependency) {
        float translationY = dependency.getHeight() + dependency.getTranslationY();
        child.setTranslationY(translationY);
        return true;
    }
}
