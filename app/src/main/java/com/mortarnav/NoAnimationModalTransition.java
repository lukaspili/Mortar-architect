package com.mortarnav;

import android.animation.AnimatorSet;
import android.view.View;

import architect.transition.BaseModalTransition;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NoAnimationModalTransition extends BaseModalTransition<View> {

    private boolean hideViewBelow;

    public NoAnimationModalTransition(boolean hideViewBelow) {
        this.hideViewBelow = hideViewBelow;
    }

    @Override
    public void configure(AnimatorSet set) {
        set.setDuration(0);
    }

    @Override
    public void show(View view, AnimatorSet set) {
    }

    @Override
    public void hide(View view, AnimatorSet set) {
    }

    @Override
    public boolean hideViewBelow() {
        return hideViewBelow;
    }
}
