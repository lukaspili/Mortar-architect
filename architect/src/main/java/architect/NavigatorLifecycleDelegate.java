package architect;

import android.content.Intent;
import android.os.Bundle;

/**
 * Hook up Navigator to the Android lifecyle
 * Can be used both in Activity (call the equivalent Activity methods) and in ViewPresenter
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorLifecycleDelegate {

    private static final String HISTORY_KEY =
            Navigator.class.getSimpleName() + "_history";

    private final Navigator navigator;

    public NavigatorLifecycleDelegate(Navigator navigator) {
        this.navigator = navigator;
    }

    public void onCreate(Intent intent, Bundle savedInstanceState, NavigatorView containerView, ScreenPath... defaultPaths) {
        Preconditions.checkNotNull(containerView, "Container view cannot not be null");
        Preconditions.checkArgument(defaultPaths != null && defaultPaths.length > 0, "Default path cannot not be null nor empty");

        if (navigator.history.shouldInit()) {
            Bundle bundle = null;
            if (intent != null && intent.hasExtra(HISTORY_KEY)) {
                bundle = intent.getBundleExtra(HISTORY_KEY);
            } else if (savedInstanceState != null && savedInstanceState.containsKey(HISTORY_KEY)) {
                bundle = savedInstanceState.getBundle(HISTORY_KEY);
            }

            if (bundle != null) {
                navigator.history.init(bundle);
            } else {
                navigator.history.init(defaultPaths);
            }
        }

        navigator.presenter.attach(containerView);
        navigator.dispatcher.activate();
    }

    //TODO: copy past from Flow, but not tested
    public void onNewIntent(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent may not be null");
        if (navigator.history.shouldInit() && intent.hasExtra(HISTORY_KEY)) {
            navigator.history.init(intent.getBundleExtra(HISTORY_KEY));
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        Preconditions.checkNotNull(outState, "SaveInstanceState bundle may not be null");
        Bundle bundle = navigator.history.toBundle();
        if (bundle != null) {
            outState.putBundle(HISTORY_KEY, bundle);
        }
    }

    public void onStart() {
        Logger.d("Lifecycle onStart");
        navigator.presenter.activate();
        navigator.dispatcher.startDispatch();
    }

    public void onStop() {
        Logger.d("Lifecycle onStop");
        navigator.presenter.desactivate();
    }

    public void onDestroy() {
        navigator.dispatcher.desactivate();
        navigator.presenter.detach();
    }

    public boolean onBackPressed() {
        if (navigator.presenter.containerViewOnBackPressed()) {
            return true;
        }

        return navigator.back();
    }
}
