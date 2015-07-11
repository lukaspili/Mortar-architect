package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.DaggerScope;
import com.mortarnav.stackable.HomeStackable;
import com.mortarnav.view.HomeNestedView;

import java.util.Random;

import javax.inject.Inject;

import architect.robot.AutoStackable;
import autodagger.AutoComponent;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoStackable(
        component = @AutoComponent(dependencies = HomeStackable.Component.class)
)
@DaggerScope(HomeNestedPresenter.class)
public class HomeNestedPresenter extends ViewPresenter<HomeNestedView> {

    private final int random;

    @Inject
    public HomeNestedPresenter() {
        random = new Random().nextInt(100);
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        int r = savedInstanceState != null ? savedInstanceState.getInt("random") : random;
        getView().randomTextView.setText("Random " + r);
    }

    @Override
    protected void onSave(Bundle outState) {
        outState.putInt("random", random);
    }

    public void click() {
        Timber.d("Clicked on home subcontent");
    }
}
