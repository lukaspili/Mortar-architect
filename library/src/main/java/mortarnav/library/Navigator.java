package mortarnav.library;

import android.content.Context;

import mortar.MortarScope;
import mortarnav.library.screen.Screen;
import mortarnav.library.screen.ScreenContextFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Navigator {

    public static final String SERVICE_NAME = Navigator.class.getName();

    public static Navigator get(Context context) {
        return MortarScope.getScope(context).getService(SERVICE_NAME);
    }

    private final NavigatorLifecycleDelegate delegate;
    private final History history;
    private final ScreenContextFactory contextFactory;
    private final NavigatorTransitions transitions;
    private final NavigatorContainerManager containerManager;
    private final Dispatcher dispatcher;

    public Navigator() {
        delegate = new NavigatorLifecycleDelegate(this);
        history = new History();
        contextFactory = new ScreenContextFactory();
        transitions = new NavigatorTransitions();
        containerManager = new NavigatorContainerManager(transitions);
        dispatcher = new Dispatcher(history, containerManager, contextFactory);
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

    void setHistory(History newHistory) {
        history.replaceBy(newHistory);
        dispatcher.dispatch();
    }

    History getHistory() {
        return history;
    }

    ScreenContextFactory getContextFactory() {
        return contextFactory;
    }

    NavigatorContainerManager getContainerManager() {
        return containerManager;
    }

    public NavigatorTransitions transitions() {
        return transitions;
    }
}
