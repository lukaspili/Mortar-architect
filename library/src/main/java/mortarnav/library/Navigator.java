package mortarnav.library;

import android.content.Context;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Navigator {

    public static final String SERVICE_NAME = Navigator.class.getName();

    public static Navigator get(Context context) {
        return MortarScope.getScope(context).getService(SERVICE_NAME);
    }

    final NavigatorContainerManager containerManager;
    private final NavigatorLifecycleDelegate delegate;
    private final History history;
    private final Dispatcher dispatcher;

    public Navigator() {
        containerManager = new NavigatorContainerManager();
        delegate = new NavigatorLifecycleDelegate(this);
        history = new History();
        dispatcher = new Dispatcher(history, containerManager);
    }

    public void push(Screen screen) {
        checkInit();

        history.push(screen);
        dispatcher.dispatch();
    }

    public boolean back() {
        checkInit();
        if (!history.canPop()) {
            return false;
        }

        history.pop();
        dispatcher.dispatch();

        return true;
    }

    private void checkInit() {
        Preconditions.checkNotNull(history, "History not set, did you forget to call delegate onCreate()?");
    }

    public NavigatorLifecycleDelegate delegate() {
        return delegate;
    }

    void init(History defaultHistory) {
        history.from(defaultHistory);
        dispatcher.dispatch();
    }
}
