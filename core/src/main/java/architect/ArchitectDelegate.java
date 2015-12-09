package architect;

import android.content.Intent;
import android.os.Bundle;

import architect.service.Service;

/**
 * Hook up Architect to the Android lifecyle
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ArchitectDelegate {

    private static final String HISTORY_KEY = Architect.class.getSimpleName() + "_history";

    private Architect architect;

    void set(Architect architect) {
        Preconditions.checkNull(this.architect, "Architect already set");
        this.architect = architect;
    }

    public void onCreate(Intent intent, Bundle savedInstanceState, Attachments attachments, Stack initialStack) {
        Preconditions.checkNotNull(architect, "Architect not set");

        Bundle bundle = null;
        if (intent != null && intent.hasExtra(HISTORY_KEY)) {
            bundle = intent.getBundleExtra(HISTORY_KEY);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(HISTORY_KEY)) {
            bundle = savedInstanceState.getBundle(HISTORY_KEY);
        }

        if (bundle != null) {
            architect.history.populateFromBundle(bundle);
        } else {
            architect.history.populateFromStack(initialStack);
        }

        architect.attachments.take(attachments);
        architect.dispatcher.activate();
    }

    //TODO: to be tested
    public void onNewIntent(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent may not be null");
        if (intent.hasExtra(HISTORY_KEY)) {
            architect.history.populateFromBundle(intent.getBundleExtra(HISTORY_KEY));
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
//        Logger.d("Lifecycle onStart");
//        architect.presenter.activate();
//        architect.dispatcher.startDispatch();
    }

    public void onStop() {
//        Logger.d("Lifecycle onStop");
//        architect.presenter.desactivate();
    }

    public void onDestroy() {
        architect.attachments.drop();
        architect.dispatcher.desactivate();

//        architect.presenter.detach();
    }

    public boolean onBackPressed() {
        if (architect.services.handlesOnBackPressed()) {
            return true;
        }

        return false;
    }
}
