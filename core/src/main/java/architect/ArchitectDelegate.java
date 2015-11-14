package architect;

import android.content.Intent;
import android.os.Bundle;

/**
 * Hook up Navigator to the Android lifecyle
 * Can be used both in Activity (call the equivalent Activity methods) and in ViewPresenter
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ArchitectDelegate {

    private static final String HISTORY_KEY = Architect.class.getSimpleName() + "_history";

    private final Architect architect;

    public ArchitectDelegate(Architect architect) {
        this.architect = architect;
    }

    public void onCreate(Intent intent, Bundle savedInstanceState) {
        Bundle bundle = null;
        if (intent != null && intent.hasExtra(HISTORY_KEY)) {
            bundle = intent.getBundleExtra(HISTORY_KEY);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(HISTORY_KEY)) {
            bundle = savedInstanceState.getBundle(HISTORY_KEY);
        }

        if (bundle != null) {
            architect.history.fromBundle(bundle);
        }

        architect.presenter.attach(containerView);
        architect.dispatcher.activate();
    }

    //TODO: test
    public void onNewIntent(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent may not be null");
        if (intent.hasExtra(HISTORY_KEY)) {
            architect.history.fromBundle(intent.getBundleExtra(HISTORY_KEY));
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        Preconditions.checkNotNull(outState, "SaveInstanceState bundle may not be null");
        Bundle bundle = architect.history.toBundle();
        if (bundle != null) {
            outState.putBundle(HISTORY_KEY, bundle);
        }
    }

    public void onStart() {
        Logger.d("Lifecycle onStart");
        architect.presenter.activate();
        architect.dispatcher.startDispatch();
    }

    public void onStop() {
        Logger.d("Lifecycle onStop");
        architect.presenter.desactivate();
    }

    public void onDestroy() {
        architect.dispatcher.desactivate();
        architect.presenter.detach();
    }

    public boolean onBackPressed() {
        if (architect.presenter.containerViewOnBackPressed()) {
            return true;
        }

        return architect.pop();
    }
}
