package architect;

import android.content.Intent;
import android.os.Bundle;

/**
 * Bridge between a navigator and Android
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

    public void onCreate(Intent intent, Bundle savedInstanceState, NavigatorView containerView, StackPath defaultPath) {
        Preconditions.checkNotNull(containerView, "Container view cannot not be null");
        Preconditions.checkNotNull(defaultPath, "Default path cannot not be null");

        if (navigator.history.isEmpty()) {
            History history = null;
            if (!navigator.config.dontRestoreStackAfterKill) {
                if (intent != null && intent.hasExtra(HISTORY_KEY)) {
                    history = History.fromBundle(intent.getBundleExtra(HISTORY_KEY));
                } else if (savedInstanceState != null && savedInstanceState.containsKey(HISTORY_KEY)) {
                    history = History.fromBundle(savedInstanceState.getBundle(HISTORY_KEY));
                }
            }

            if (history != null) {
                navigator.history.copy(history);
            } else {
                navigator.history.add(defaultPath, History.NAV_TYPE_PUSH);
            }
        }

        navigator.presenter.attach(containerView);
    }

    //TODO: copy past from Flow, but not tested
    public void onNewIntent(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent may not be null");
        if (navigator.history.isEmpty() && intent.hasExtra(HISTORY_KEY)) {
            History history = History.fromBundle(intent.getBundleExtra(HISTORY_KEY));
            navigator.history.copy(history);
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
        navigator.dispatcher.dispatch();
    }

    public void onStop() {
        Logger.d("Lifecycle onStop");
        navigator.presenter.desactivate();
    }

    public void onDestroy() {
        navigator.presenter.detach();
    }

    public boolean onBackPressed() {
        if (navigator.presenter.containerViewOnBackPressed()) {
            return true;
        }

        return navigator.back();
    }
}
