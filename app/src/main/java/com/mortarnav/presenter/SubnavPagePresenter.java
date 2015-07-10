package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.DaggerScope;
import com.mortarnav.presenter.stackable.SubnavPageStackable;
import com.mortarnav.view.SubnavPageView;

import java.util.Random;

import architect.Navigator;
import architect.robot.AutoStackable;
import architect.robot.FromPath;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStackable(
        component = @AutoComponent(dependencies = SubnavPresenter.class),
        pathWithView = SubnavPageView.class
)
@DaggerScope(SubnavPagePresenter.class)
public class SubnavPagePresenter extends ViewPresenter<SubnavPageView> {

    private final String title;

    public SubnavPagePresenter(@FromPath String title) {
        this.title = title;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        getView().textView.setText("Subnav page view: " + title);
    }

    public void next() {
        Navigator.get(getView()).push(new SubnavPageStackable("random " + new Random().nextInt(100)));
    }
}
