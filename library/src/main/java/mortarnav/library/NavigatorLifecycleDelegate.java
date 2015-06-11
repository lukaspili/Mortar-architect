package mortarnav.library;

import android.content.Intent;
import android.os.Bundle;

/**
 * Bridge between a navigator and Android
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorLifecycleDelegate {

    private static final String HISTORY_KEY =
            NavigatorLifecycleDelegate.class.getSimpleName() + "_history";

    private final Navigator navigator;

    public NavigatorLifecycleDelegate(Navigator navigator) {
        this.navigator = navigator;
    }

    public void onCreate(Intent intent, Bundle savedInstanceState, NavigatorContainerView containerView, Screen defaultScreen) {
        Preconditions.checkNotNull(containerView, "Container view may not be null");
        Preconditions.checkNotNull(defaultScreen, "Default screen may not be null");

        if (navigator.history.isEmpty()) {
            History history;
            if (intent != null && intent.hasExtra(HISTORY_KEY)) {
                history = History.fromBundle(intent.getBundleExtra(HISTORY_KEY));
            } else if (savedInstanceState != null && savedInstanceState.containsKey(HISTORY_KEY)) {
                history = History.fromBundle(savedInstanceState.getBundle(HISTORY_KEY));
            } else {
                history = History.create(defaultScreen);
            }
            navigator.history.replaceBy(history);
        }

        // set the container, which triggers a dispatch
        navigator.containerManager.setContainerView(containerView);
    }

    //TODO: copy past from Flow, but not tested
    public void onNewIntent(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent may not be null");
        if (intent.hasExtra(HISTORY_KEY)) {
            History history = History.fromBundle(intent.getBundleExtra(HISTORY_KEY));
            navigator.history.replaceBy(history);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        Preconditions.checkNotNull(outState, "SaveInstanceState bundle may not be null");
        Bundle bundle = navigator.history.toBundle();
        if (bundle != null) {
            outState.putBundle(HISTORY_KEY, bundle);
        }
    }

    public void onDestroy() {
        navigator.containerManager.removeContainerView();
    }

    public boolean onBackPressed() {
        if (navigator.containerManager.containerViewOnBackPressed()) {
            return true;
        }

        return navigator.back();
    }
}
