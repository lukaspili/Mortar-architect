package mortarnav.library;

/**
 * Bridge between a navigator and Android
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorLifecycleDelegate {

    private final Navigator navigator;

    public NavigatorLifecycleDelegate(Navigator navigator) {
        this.navigator = navigator;
    }

    public void onCreate(NavigatorContainerView containerView, Screen defaultScreen) {
        navigator.dispatcher().configureContainerView(containerView);
        navigator.init(History.create(defaultScreen));
    }

    public void onDestroy() {
        navigator.dispatcher().removeContainerView();
    }

    public boolean onBackPressed() {
        return navigator.back();
    }
}
