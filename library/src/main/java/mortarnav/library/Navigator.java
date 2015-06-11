package mortarnav.library;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import mortar.MortarScope;
import mortar.Scoped;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Navigator implements Scoped {

    public static final String SCOPE_NAME = Navigator.class.getName();
    public static final String SERVICE_NAME = Navigator.class.getName();

    public static Navigator get(Context context) {
        //noinspection ResourceType
        return (Navigator) context.getSystemService(SERVICE_NAME);
    }

    public static Navigator get(View view) {
        return get(view.getContext());
    }

    /**
     * Retreive the navigator from the nearest child of the current context
     * Use this method from the host of a navigator container view to retrieve the associated navigator
     */
    public static Navigator find(Context context) {
        MortarScope scope = MortarScope.findChild(context, SCOPE_NAME);
        return scope != null ? scope.<Navigator>getService(SERVICE_NAME) : null;
    }

    public static Navigator create(MortarScope containerScope) {
        Navigator navigator = new Navigator();

        MortarScope scope = containerScope.buildChild()
                .withService(SERVICE_NAME, navigator)
                .build(SCOPE_NAME);
        scope.register(navigator);

        return navigator;
    }

    final History history;
    final ScreenContextFactory contextFactory;
    final Transitions transitions;
    final ContainerManager containerManager;
    final NavigatorLifecycleDelegate delegate;
    final Dispatcher dispatcher;
    private MortarScope scope;

    private Navigator() {
        history = new History();
        contextFactory = new ScreenContextFactory();
        transitions = new Transitions();
        delegate = new NavigatorLifecycleDelegate(this);
        dispatcher = new Dispatcher(this);
        containerManager = new ContainerManager(dispatcher, transitions);
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

    /**
     * Scope can be null if the method is called after the navigator scope was destroyed
     */
    @Nullable
    MortarScope getScope() {
        return scope;
    }

    public Transitions transitions() {
        return transitions;
    }


    // Scoped

    @Override
    public void onEnterScope(MortarScope scope) {
        this.scope = scope;
    }

    @Override
    public void onExitScope() {
        dispatcher.stop();
        this.scope = null;
    }
}
