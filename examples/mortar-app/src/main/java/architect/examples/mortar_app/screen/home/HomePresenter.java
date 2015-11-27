package architect.examples.mortar_app.screen.home;

import android.os.Bundle;

import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomePresenter extends ViewPresenter<HomeView> {

//    private static int count = 0;
//
//    //    @NavigationParam
//    private final String name;
//
//    //    @NavigationResult
//    private final String result;
//
//    //    @ScreenData
//    private final HomeState state;

//    public HomePresenter(HomeState state, String name, String result) {
//        this.state = state;
//        this.name = name;
//        this.result = result;
//
//        if (result != null) {
//            Timber.d("Home presenter with result: %s", result);
//        }
//    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
//        Timber.d("Home onLoad with random = %d", state.random);
//
//        getView().titleTextView.setText(name);
//        getView().subtitleTextView.setText("Random number: " + state.random);
    }

//    @Parcel(parcelsIndex = false)
//    public static class HomeState {
//
//        int random;
//
//        public HomeState() {
//            this.random = new Random().nextInt(100);
//        }
//
//        @ParcelConstructor
//        public HomeState(int random) {
//            this.random = random;
//        }
//    }
}
