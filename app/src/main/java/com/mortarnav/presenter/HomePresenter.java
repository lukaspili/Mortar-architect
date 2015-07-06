package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.path.HomePath;
import com.mortarnav.presenter.scope.path.MyPopupPath;
import com.mortarnav.presenter.scope.path.SlidesPath;
import com.mortarnav.presenter.scope.path.SubnavPath;
import com.mortarnav.view.HomeView;

import java.util.Random;

import architect.Navigator;
import architect.autostack.StackParam;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomePresenter extends ViewPresenter<HomeView> {

    private static int count = 0;

    private final String name;
    private final int random;

    public HomePresenter(@StackParam String name) {
        this.name = name;
        random = new Random().nextInt(100);
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        getView().titleTextView.setText(name);

        int r = savedInstanceState != null ? savedInstanceState.getInt("random") : random;
        getView().subtitleTextView.setText("Random number: " + r);
    }

    @Override
    protected void onSave(Bundle outState) {
        outState.putInt("random", random);
    }

    public void nextHomeClick() {
        Navigator.get(getView()).push(new HomePath("Home " + ++count));
    }

    public void subnavClick() {
        Navigator.get(getView()).push(new SubnavPath());
    }

    public void customViewClick() {
        Timber.d("Click from custom view");
    }

    public void pagerClick() {
        Navigator.get(getView()).push(new SlidesPath());
    }

    public void showPopupClick() {
        Navigator.get(getView()).show(new MyPopupPath());
    }

    public void replaceNewHomeClick() {
        Navigator.get(getView()).replace(new HomePath("Replaced!"));
    }
}
