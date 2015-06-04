package mortarnav.library;

import android.content.Context;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Navigator implements Dispatcher.NavigationCallback {

    public static final String SERVICE_NAME = Navigator.class.getName();

    public static Navigator get(Context context) {
        return MortarScope.getScope(context).getService(SERVICE_NAME);
    }

    private final NavigatorLifecycleDelegate delegate;
    private final Dispatcher dispatcher;
    private History history;

    public Navigator() {
        delegate = new NavigatorLifecycleDelegate(this);
        dispatcher = new Dispatcher(this);
    }

    public void push(Screen screen) {
        checkInit();

        history.push(screen);
        dispatcher.enqueue(screen);
    }

    public boolean back() {
        checkInit();
        if (!history.canPop()) {
            return false;
        }

        history.pop();
        dispatcher.enqueue(history.peek());

        return true;
    }

    private void checkInit() {
        Preconditions.checkNotNull(history, "History not set, did you forget to call delegate onCreate()?");
    }

    public NavigatorLifecycleDelegate delegate() {
        return delegate;
    }

    Dispatcher dispatcher() {
        return dispatcher;
    }

    void init(History history) {
        this.history = history;
        dispatcher.enqueue(history.peek());
    }


    // Dispatcher.NavigationCallback

    @Override
    public void onDispatched(Dispatcher.PendingNavigation pendingNavigation) {
//        switch (pendingNavigation.getDirection()) {
//            case FORWARD:
//                history.push(pendingNavigation.getDestination());
//                break;
//            case BACKWARD:
//                history.pop();
//                break;
//            case REPLACE:
//
//        }
    }
}
