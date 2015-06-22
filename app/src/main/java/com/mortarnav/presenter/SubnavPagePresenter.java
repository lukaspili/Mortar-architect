package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.DaggerScope;
import com.mortarnav.presenter.scope.path.SubnavPagePath;
import com.mortarnav.view.SubnavPageView;

import java.util.Random;

import autodagger.AutoComponent;
import mortar.ViewPresenter;
import mortarnav.Navigator;
import mortarnav.autopath.AutoPath;
import mortarnav.autoscope.AutoScope;
import mortarnav.autoscope.PathParam;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoScope(
        component = @AutoComponent(dependencies = SubnavPresenter.class),
        path = @AutoPath(withView = SubnavPageView.class)
)
@DaggerScope(SubnavPagePresenter.class)
public class SubnavPagePresenter extends ViewPresenter<SubnavPageView> {

    private final String title;

    public SubnavPagePresenter(@PathParam String title) {
        this.title = title;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        getView().textView.setText("Subnav page view: " + title);
    }

    public void next() {
        Navigator.get(getView()).push(new SubnavPagePath("random " + new Random().nextInt(100)));
    }
}
