package com.mortarnav.mvp.home;

import android.os.Bundle;

import org.parceler.Parcel;

import java.util.Random;

import architect.Navigator;
import architect.robot.NavigationParam;
import architect.robot.NavigationResult;
import architect.robot.PresenterState;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomePresenter extends ViewPresenter<HomeView> {

    private static int count = 0;

    @NavigationParam
    private final String name;

    @NavigationResult
    private final String result;

    @PresenterState
    private final HomeState state;

    public HomePresenter(String name, HomeState state) {
        this.name = name;
        this.state = state;
        this.result = null;
    }

    public HomePresenter(String name, HomeState state, String result) {
        this.name = name;
        this.state = state;
        this.result = result;

        Timber.d("Home presenter with result: %s", result);
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        Timber.d("Home onLoad with random = %d", state.random);

        getView().titleTextView.setText(name);

//        int r = savedInstanceState != null ? savedInstanceState.getInt("random") : random;
        getView().subtitleTextView.setText("Random number: " + state.random);
    }

    @Override
    protected void onSave(Bundle outState) {
//        outState.putInt("random", random);
    }

    public void nextHomeClick() {
        Navigator.get(getView()).push(new HomeScreen("Home " + ++count));
    }


    public void subnavClick() {
//        Navigator.get(getView()).push(new SubnavStackable());
    }

    public void customViewClick() {
        Timber.d("Click from custom view");
    }

    public void pagerClick() {
//        Navigator.get(getView()).push(new SlidesStackable());
    }

    public void showPopupClick() {
//        Navigator.get(getView()).show(new MyPopupStackable());
    }

    public void replaceNewHomeClick() {
        Navigator.get(getView()).replace(new HomeScreen("Replaced!"));
    }

    public void showReturnsResultClick() {
//        Navigator.get(getView()).push(new ReturnsResultStackable());
    }

    public void backToRootClick() {
        Navigator.get(getView()).back("This is a navigation result");
//        Navigator.get(getView()).backToRoot();
    }

    public void showTwoPopupsClick() {
//        Navigator.get(getView()).show(new MyPopup2Stackable(), new MyPopupStackable());
    }

    public void showPopupTwoClick() {
//        Navigator.get(getView()).show(new MyPopup2Stackable());
    }

    public void setNewStackClick() {
//        Navigator.get(getView()).set(new ScreenPathsStack()
//                .put(new HomeScreen("NEW STACK 1"))
//                .put(new HomeScreen("NEW STACK 2"))
//                .put(new SlidesStackable()), ViewTransitionDirection.FORWARD);
    }

    @Parcel(parcelsIndex = false)
    public static class HomeState {

        public HomeState() {
            Timber.d("NEW HomeState, random = %d", random);
        }

        int random = new Random().nextInt(100);
    }
}
