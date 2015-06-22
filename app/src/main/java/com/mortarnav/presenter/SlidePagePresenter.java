package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.DaggerScope;
import com.mortarnav.view.SlidePageView;

import autodagger.AutoComponent;
import mortar.ViewPresenter;
import mortarnav.autopath.AutoPath;
import mortarnav.autoscope.AutoStack;
import mortarnav.autoscope.StackParam;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStack(
        component = @AutoComponent(dependencies = SlidesPresenter.class),
        path = @AutoPath(withView = SlidePageView.class)
)
@DaggerScope(SlidePagePresenter.class)
public class SlidePagePresenter extends ViewPresenter<SlidePageView> {

    private final int id;

    public SlidePagePresenter(@StackParam int id) {
        this.id = id;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        String title;
        int color;
        switch (id) {
            case 1:
                title = "Page One";
                color = android.R.color.holo_blue_bright;
                break;
            case 2:
                title = "Page Two";
                color = android.R.color.holo_orange_dark;
                break;
            case 3:
            default:
                title = "Page Three";
                color = android.R.color.holo_red_dark;
                break;
        }

        getView().textView.setText(title);
        getView().setBackgroundColor(getView().getResources().getColor(color));
    }
}
