//package architect.examples.simple_app.mvp.home;
//
//import android.os.Bundle;
//
//import architect.examples.simple_app.mvp.popup1.screen.Popup1Screen;
//import architect.examples.simple_app.mvp.slides.screen.SlidesScreen;
//
//import org.parceler.Parcel;
//import org.parceler.ParcelConstructor;
//
//import java.util.Random;
//
//import architect.Architect;
//import architect.Navigation;
//import mortar.ViewPresenter;
//import timber.log.Timber;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
////@AutoScreen(
////        component = @AutoComponent(includes = StandardAutoComponent.class),
////        pathView = HomeView.class,
////        containsSubscreens = {
////                @ContainsSubscreen(type = BannerScreen.class, name = "bannerScreen"),
////                @ContainsSubscreen(type = BannerScreen.class, name = "bannerScreen2")
////        }
////)
//public class HomePresenter extends ViewPresenter<HomeView> {
//
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
//
//    public HomePresenter(HomeState state, String name, String result) {
//        this.state = state;
//        this.name = name;
//        this.result = result;
//
//        if (result != null) {
//            Timber.d("Home presenter with result: %s", result);
//        }
//    }
//
//    @Override
//    protected void onLoad(Bundle savedInstanceState) {
//        Timber.d("Home onLoad with random = %d", state.random);
//
//        getView().titleTextView.setText(name);
//        getView().subtitleTextView.setText("Random number: " + state.random);
//    }
//
//    public void nextHomeClick() {
////        Navigator.get(getView()).push(new HomeScreen("Home " + ++count));
//        Architect.get(getView()).push(new HomeScreen("Home " + ++count), new HomeScreen("Home " + ++count));
//    }
//
//
//    public void subnavClick() {
//        Architect.get(getView()).navigate(new Navigation()
//                .push(new HomeScreen("Nav pushed 1"))
//                .push(new HomeScreen("Nav pushed 2"))
//                .replace(new HomeScreen("Nav replace 1"))
//                .push(new HomeScreen("Nav pushed 3")));
//
//
////        Navigator.get(getView()).push(new SubnavStackable());
//    }
//
//    public void customViewClick() {
//        Timber.d("Click from custom view");
//    }
//
//    public void pagerClick() {
//        Architect.get(getView()).push(new SlidesScreen("test param1", "test param2"));
//    }
//
//    public void showPopupClick() {
//        Architect.get(getView()).show(new Popup1Screen());
//    }
//
//    public void replaceNewHomeClick() {
//        Architect.get(getView()).replace(new HomeScreen("Replaced 2!"));
////        Navigator.get(getView()).replace(new HomeScreen("Replaced!"), new HomeScreen("Replaced 2!"));
//    }
//
//    public void showReturnsResultClick() {
////        Navigator.get(getView()).push(new ReturnsResultStackable());
//    }
//
//    public void backToRootClick() {
////        Navigator.get(getView()).back("This is a navigation result");
//        Architect.get(getView()).backToRoot("This is a navigation result backtoroot");
//    }
//
//    public void showTwoPopupsClick() {
////        Navigator.get(getView()).show(new MyPopup2Stackable(), new MyPopupStackable());
//    }
//
//    public void showPopupTwoClick() {
////        Navigator.get(getView()).show(new MyPopup2Stackable());
//    }
//
//    public void setNewStackClick() {
//        Architect.get(getView()).set(new HomeScreen("NEW STACK 1"),
//                new HomeScreen("NEW STACK 2"),
//                new HomeScreen("NEW STACK 3"));
//    }
//
//
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
//}
