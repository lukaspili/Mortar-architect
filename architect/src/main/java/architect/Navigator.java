package architect;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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
        return create(containerScope, null);
    }

    public static Navigator create(MortarScope containerScope, Config config) {
        if (config == null) {
            config = new Config();
        }
        Navigator navigator = new Navigator(config);

        MortarScope scope = containerScope.buildChild()
                .withService(SERVICE_NAME, navigator)
                .build(SCOPE_NAME);
        scope.register(navigator);

        return navigator;
    }

    final Config config;
    final History history;
    final Transitions transitions;
    final Presenter presenter;
    final NavigatorLifecycleDelegate delegate;
    final Dispatcher dispatcher;
    private MortarScope scope;

    private Navigator(Config config) {
        this.config = config;
        history = new History();
        transitions = new Transitions();
        delegate = new NavigatorLifecycleDelegate(this);
        dispatcher = new Dispatcher(this);
        presenter = new Presenter(transitions);
    }

    public void push(StackPath path) {
        add(path, History.NAV_TYPE_PUSH);
    }

    public void show(StackPath path) {
        add(path, History.NAV_TYPE_MODAL);
    }

//    public boolean back() {
//        return back(true);
//    }

    public void chain(NavigationChain chain) {
        Preconditions.checkArgument(!chain.chains.isEmpty(), "Navigation chain cannot be empty");

        List<History.Entry> entries = new ArrayList<>(chain.chains.size());
        for (int i = 0; i < chain.chains.size(); i++) {
            NavigationChain.Chain c = chain.chains.get(i);
            if (c.path == null) {
                if (history.canKill()) {
                    entries.add(history.kill());
                }
            } else {
                entries.add(history.add(c.path, History.NAV_TYPE_PUSH));
            }
        }

        dispatcher.dispatch(entries);
    }

    private void add(StackPath path, int navType) {
        Preconditions.checkNotNull(scope, "Navigator scope cannot be null");

        History.Entry next = history.add(path, navType);
        dispatcher.dispatch(next);
    }

    public boolean back() {
        Preconditions.checkNotNull(scope, "Navigator scope cannot be null");

        if (!history.canKill()) {
            return false;
        }

        History.Entry entry = history.kill();
        dispatcher.dispatch(entry);

        return true;
    }

    /**
     * Scope can be null if the method is called after the navigator scope was destroyed
     * //TODO: is it really possible to be null?
     */
    MortarScope getScope() {
        return scope;
    }

    public NavigatorLifecycleDelegate delegate() {
        return delegate;
    }

    public Transitions transitions() {
        return transitions;
    }


    // Scoped

    @Override
    public void onEnterScope(MortarScope scope) {
        Preconditions.checkNull(this.scope, "Cannot register navigator multiple times in a scope");
        this.scope = scope;
    }

    /**
     * Scope associated to navigator is destroyed
     * Everything will be destroyed
     */
    @Override
    public void onExitScope() {
        Logger.d("Navigation scope exit");

        // stop and kill the dispatcher
        dispatcher.kill();

        scope = null;
    }

    public static class Config {

        /**
         * After process kill, the previous stack won't be restored
         * and the app will start from the beginning again
         * The advantage of this is that it allows the developer to not care
         * about saving and restoring state in presenters' bundles at all
         * Default value is false, the stack will be restored
         */
        boolean dontRestoreStackAfterKill;

        public Config dontRestoreStackAfterKill(boolean dontRestoreStackAfterKill) {
            this.dontRestoreStackAfterKill = dontRestoreStackAfterKill;
            return this;
        }
    }
}
