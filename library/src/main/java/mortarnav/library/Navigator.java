package mortarnav.library;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import mortar.MortarScope;
import mortar.Scoped;
import mortarnav.library.screen.Screen;

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

    public static Navigator find(Context context) {
        MortarScope scope = MortarScope.findChild(context, SCOPE_NAME);
        return scope != null ? scope.<Navigator>getService(SERVICE_NAME) : null;
    }

    public static Navigator create(MortarScope parentScope) {
        Navigator navigator = new Navigator();

        MortarScope scope = parentScope.buildChild()
                .withService(SERVICE_NAME, navigator)
                .build(SCOPE_NAME);
        scope.register(navigator);

        return navigator;
    }

    final History history;
    final ScreenContextFactory contextFactory;
    final NavigatorTransitions transitions;
    final NavigatorContainerManager containerManager;
    final NavigatorLifecycleDelegate delegate;
    final Dispatcher dispatcher;
    private MortarScope scope;

    private Navigator() {
        history = new History();
        contextFactory = new ScreenContextFactory();
        transitions = new NavigatorTransitions();
        containerManager = new NavigatorContainerManager(transitions);
        delegate = new NavigatorLifecycleDelegate(this);
        dispatcher = new Dispatcher(this);
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

    void setNewHistory(History newHistory) {
        history.replaceBy(newHistory);
        dispatcher.dispatch();
    }

    @Nullable
    MortarScope getScope() {
        return scope;
    }

    public NavigatorTransitions transitions() {
        return transitions;
    }


    // Scoped

    @Override
    public void onEnterScope(MortarScope scope) {
        this.scope = scope;
    }

    @Override
    public void onExitScope() {
        this.scope = null;
        dispatcher.stop();
    }
}
