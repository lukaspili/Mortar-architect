package mortarnav.library;

/**
 * Bridge between a navigator and Android
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorLifecycleDelegate {

    private final Navigator navigator;
    private NavigatorContainerView containerView;

    public NavigatorLifecycleDelegate(Navigator navigator) {
        this.navigator = navigator;
    }

    public void onCreate(NavigatorContainerView containerView, Screen defaultScreen) {
        this.containerView = containerView;

        navigator.set(defaultScreen);
    }
}
