package com.mortarnav.presenter;

import android.os.Bundle;

import com.mortarnav.stackable.BannerScreen;
import com.mortarnav.stackable.HomeScreen;
import com.mortarnav.view.HomeView;

import org.parceler.Parcel;

import java.util.Random;

import architect.Navigator;
import architect.commons.ScreenService;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomePresenter extends ViewPresenter<HomeView> {

    private static int count = 0;

    private final String name;
    private final String result;
    private HomeState state;

    public HomePresenter(String name, String result) {
        this.name = name;
        this.result = result;

        if (result != null) {
            Timber.d("Home presenter with result: %s", result);
        }
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        state = ScreenService.get(getView());
        if (state.random == -1) {
            state.random = new Random().nextInt(100);
        }

        getView().titleTextView.setText(name);

//        int r = savedInstanceState != null ? savedInstanceState.getInt("random") : random;
        getView().subtitleTextView.setText("Random number: " + state.random);
    }

    @Override
    protected void onSave(Bundle outState) {
//        outState.putInt("random", random);
    }

//    @Override
//    public void onReceivedResult(String result) {
//        Timber.d("Receive result: %s", result);
//        // beware that this is called before onLoad() and getView() returns null here
//    }

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
        Navigator.get(getView()).backToRoot();
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
    public static class HomeState implements BannerScreen.StateWithBannerScreen {

        int random = -1;

        BannerScreen bannerScreen = new BannerScreen();

        @Override
        public BannerScreen getBannerScreen() {
            return bannerScreen;
        }
    }
}
