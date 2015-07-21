package com.mortarnav;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.Toolbar;
import android.view.View;

import javax.inject.Inject;

import autodagger.AutoInjector;
import mortar.Presenter;
import mortar.bundler.BundleService;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(MainActivity.class)
@DaggerScope(MainActivity.class)
public class ToolbarOwner extends Presenter<Toolbar> {

    @Inject
    public ToolbarOwner() {
    }

    public Animator animateHide() {
        if (!hasView()) return null;
        if (getView().getVisibility() == View.GONE) {
            return null;
        }
        return ObjectAnimator.ofFloat(getView(), View.Y, 0, -getView().getHeight());
    }

    public Animator animateShow() {
        if (!hasView()) return null;
        if (getView().getVisibility() == View.VISIBLE) {
            return null;
        }
        return ObjectAnimator.ofFloat(getView(), View.Y, -getView().getHeight(), 0);
    }

    public void hide() {
        if (!hasView()) return;
        getView().setVisibility(View.GONE);
    }

    public void show() {
        if (!hasView()) return;
        getView().setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        if (!hasView()) return;
        getView().setTitle(title);
    }

    @Override
    protected BundleService extractBundleService(Toolbar view) {
        return BundleService.getBundleService(view.getContext());
    }


}
