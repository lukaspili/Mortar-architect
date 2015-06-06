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
        navigator.containerManager.setContainerView(containerView);
        navigator.init(History.create(defaultScreen));
    }

    public void onDestroy() {


    }

    public boolean onBackPressed() {
        return navigator.back();
    }
}
