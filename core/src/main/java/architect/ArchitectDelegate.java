package architect;

import android.content.Intent;
import android.os.Bundle;

/**
 * Hook up Architect to the Android lifecyle
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ArchitectDelegate {

    private static final String HISTORY_KEY = Architect.class.getSimpleName() + "_history";

    private Architect architect;

    void attach(Architect architect) {
        Preconditions.checkNull(this.architect, "Architect already attached to delegate");
        this.architect = architect;
    }

    public void onCreate(Intent intent, Bundle savedInstanceState) {
        Preconditions.checkNotNull(architect, "Architect not attached to delegate");

        Bundle bundle = null;
        if (intent != null && intent.hasExtra(HISTORY_KEY)) {
            bundle = intent.getBundleExtra(HISTORY_KEY);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(HISTORY_KEY)) {
            bundle = savedInstanceState.getBundle(HISTORY_KEY);
        }

        if (bundle != null) {
            architect.history.fromBundle(bundle);
        }

//        architect.presenter.attach(containerView);
        architect.dispatcher.activate();
    }

    //TODO: to be tested
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
//        Logger.d("Lifecycle onStart");
//        architect.presenter.activate();
//        architect.dispatcher.startDispatch();
    }

    public void onStop() {
//        Logger.d("Lifecycle onStop");
//        architect.presenter.desactivate();
    }

    public void onDestroy() {
//        architect.detach();
        architect.dispatcher.desactivate();

//        architect.presenter.detach();
    }

    public boolean onBackPressed() {
        Services.Service service = architect.getTopService();
        if (service == null) {
            return false;
        }

//        if (service.get.onBackPressed()) {
//            return true;
//        }

        return service.getController().pop();
    }
}
