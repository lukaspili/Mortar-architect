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

    public static Navigator create(MortarScope containerScope, StackableParceler parceler) {
        return create(containerScope, parceler, null);
    }

    public static Navigator create(MortarScope containerScope, StackableParceler parceler, Config config) {
        if (config == null) {
            config = new Config();
        }

        Preconditions.checkNotNull(containerScope, "Mortar scope for Navigator cannot be null");
        Preconditions.checkArgument(config.dontRestoreStackAfterKill || parceler != null, "StackableParceler for Navigator cannot be null");

        Navigator navigator = new Navigator(parceler, config);

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

    private Navigator(StackableParceler parceler, Config config) {
        this.config = config;
        history = new History(parceler);
        transitions = new Transitions();
        delegate = new NavigatorLifecycleDelegate(this);
        dispatcher = new Dispatcher(this);
        presenter = new Presenter(transitions);
    }

    /**
     * Push one path
     */
    public void push(StackablePath path) {
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, path));
    }

    /**
     * Push one or several paths
     */
    public void push(StackablePath... paths) {
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, paths));
    }

    /**
     * Push navigation stack on top of the current one
     */
    public void push(NavigationStack stack) {
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, stack));
    }

    /**
     * Show one path as modal
     */
    public void show(StackablePath path) {
        dispatcher.dispatch(add(History.NAV_TYPE_MODAL, path));
    }

    /**
     * Show several paths as modal
     */
    public void show(StackablePath... paths) {
        dispatcher.dispatch(add(History.NAV_TYPE_MODAL, paths));
    }

    /**
     * Show navigation stack on top of current one
     */
    public void show(NavigationStack stack) {
        dispatcher.dispatch(add(History.NAV_TYPE_MODAL, stack));
    }

    /**
     * Replace current path with new one
     */
    public void replace(StackablePath path) {
        check();
        history.kill();
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, path));
    }

    /**
     * Replace current path with several new ones
     */
    public void replace(StackablePath... paths) {
        check();
        history.kill();
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, paths));
    }

    /**
     * Replace current path with new stack
     */
    public void replace(NavigationStack stack) {
        check();
        history.kill();
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, stack));
    }

    /**
     * Execute several navigation event on the current stack
     */
    public void chain(NavigationChain chain) {
        chain(chain, null);
    }

    /**
     * Execute several navigation event on the current stack
     */
    public void chain(NavigationChain chain, ViewTransitionDirection direction) {
        check();
        Preconditions.checkArgument(chain != null && !chain.chains.isEmpty(), "Navigation chain cannot be null nor empty");

        ViewTransitionDirection defaultDirection = null;
        List<History.Entry> entries = new ArrayList<>(chain.chains.size());
        for (int i = 0; i < chain.chains.size(); i++) {
            NavigationChain.Chain c = chain.chains.get(i);
            if (c.path == null) {
                defaultDirection = ViewTransitionDirection.BACKWARD;
                if (history.canKill()) {
                    if (c.type == NavigationChain.Chain.TYPE_BACK) {
                        entries.add(history.kill());
                    } else {
                        entries.addAll(history.killAll(false));
                    }
                }
            } else {
                defaultDirection = ViewTransitionDirection.FORWARD;
                if (c.type == NavigationChain.Chain.TYPE_REPLACE) {
                    History.Entry e = history.kill();
                    if (history.indexOf(e) != 0) {
                        entries.add(e);
                    }
                }

                // push type for push and replace, modal for show
                int type = c.type == NavigationChain.Chain.TYPE_PUSH || c.type == NavigationChain.Chain.TYPE_REPLACE ?
                        History.NAV_TYPE_PUSH : History.NAV_TYPE_MODAL;
                entries.add(history.add(c.path, type));
            }
        }

        entries.get(entries.size() - 1).direction = direction != null ? direction : defaultDirection;
        entries.get(0).returnsResult = chain.result;
        dispatcher.dispatch(entries);
    }

    /**
     * Set new navigation stack by replacing the current one
     *
     * @param direction specify the ViewTransition direction to apply
     */
    public void set(NavigationStack stack, ViewTransitionDirection direction) {
        check();

        List<History.Entry> entries = new ArrayList<>();
        entries.addAll(history.killAll(true));
        entries.addAll(add(History.NAV_TYPE_PUSH, stack));
        entries.get(entries.size() - 1).direction = direction;
        dispatcher.dispatch(entries);
    }

    public boolean back() {
        return back(null);
    }

    public boolean back(Object withResult) {
        check();
        if (!history.canKill()) {
            return false;
        }

        History.Entry entry = history.kill();
        if (withResult != null) {
            entry.returnsResult = withResult;
        }

        dispatcher.dispatch(entry);

        return true;
    }

    public boolean backToRoot() {
        return backToRoot(null);
    }

    public boolean backToRoot(Object result) {
        check();
        if (!history.canKill()) {
            return false;
        }

        List<History.Entry> entries = history.killAll(false);

        if (result != null) {
            entries.get(0).returnsResult = result;
        }
//        entries.get(entries.size() - 1).transitionDirection = TransitionDirection.BACKWARD;

        dispatcher.dispatch(entries);
        return true;
    }

    private List<History.Entry> add(int navType, NavigationStack stack) {
        Preconditions.checkArgument(stack != null && !stack.paths.isEmpty(), "Navigation stack cannot be null nor empty");
        return add(navType, stack.paths.toArray(new StackablePath[stack.paths.size()]));
    }

    private List<History.Entry> add(int navType, StackablePath... paths) {
        Preconditions.checkArgument(paths != null && paths.length > 0, "StackablePath cannot be null or empty");

        List<History.Entry> entries = new ArrayList<>(paths.length);
        for (int i = 0; i < paths.length; i++) {
            entries.add(history.add(paths[i], navType));
        }

        return entries;
    }

    private History.Entry add(int navType, StackablePath path) {
        Preconditions.checkNotNull(path, "StackablePath cannot be null");
        return history.add(path, navType);
    }

    private void check() {
        Preconditions.checkNotNull(scope, "Navigator scope cannot be null");
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
