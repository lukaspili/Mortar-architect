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

//    public static Navigator create(MortarScope containerScope, StackableParceler parceler) {
//        return create(containerScope, parceler, null);
//    }

    public static Navigator create(MortarScope containerScope, StackableParceler parceler) {
//        if (config == null) {
//            config = new Config();
//        }

        Preconditions.checkNotNull(containerScope, "Mortar scope for Navigator cannot be null");
        Preconditions.checkArgument(parceler != null, "StackableParceler for Navigator cannot be null");

        Navigator navigator = new Navigator(parceler);

        MortarScope scope = containerScope.buildChild()
                .withService(SERVICE_NAME, navigator)
                .build(SCOPE_NAME);
        scope.register(navigator);

        return navigator;
    }

    //    final Config config;
    final History history;
    final Transitions transitions;
    final Presenter presenter;
    final NavigatorLifecycleDelegate delegate;
    final Dispatcher dispatcher;
    private MortarScope scope;

    private Navigator(StackableParceler parceler) {
//        this.config = config;
        history = new History(parceler);
        transitions = new Transitions();
        delegate = new NavigatorLifecycleDelegate(this);
        dispatcher = new Dispatcher(this);
        presenter = new Presenter(transitions);
    }

    /**
     * Push one path
     */
    public void push(ScreenPath path) {
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, path, null, null), Dispatcher.DISPATCH_FORWARD, null);
    }

    /**
     * Push one or several paths
     */
    public void push(ScreenPath... paths) {
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, paths), Dispatcher.DISPATCH_FORWARD, null);
    }

    /**
     * Push one path builder
     */
    public void push(PathBuilder builder) {
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, builder.path, builder.transition, builder.id), Dispatcher.DISPATCH_FORWARD, null);
    }

    /**
     * Push several path builders
     */
    public void push(PathBuilder... builders) {
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, builders), Dispatcher.DISPATCH_FORWARD, null);
    }

//    /**
//     * Show one path as modal
//     */
//    public void show(ScreenPath path) {
//        dispatcher.dispatch(add(History.NAV_TYPE_MODAL, path), ViewTransitionDirection.FORWARD, null);
//    }
//
//    /**
//     * Show several paths as modal
//     */
//    public void show(ScreenPath... paths) {
//        dispatcher.dispatch(add(History.NAV_TYPE_MODAL, paths), ViewTransitionDirection.FORWARD, null);
//    }
//
////    /**
////     * Show navigation stack on top of current one
////     */
////    public void show(ScreenPathsStack stack) {
////        dispatcher.dispatch(add(History.NAV_TYPE_MODAL, stack), ViewTransitionDirection.FORWARD, null);
////    }
//
//    /**
//     * Replace current path with new one
//     */
//    public void replace(ScreenPath path, String transition) {
//        check();
//        history.kill();
//        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, path, transition), ViewTransitionDirection.REPLACE, null);
//    }
//
//    /**
//     * Replace current path with several new ones
//     */
//    public void replace(String transition, ScreenPath... paths) {
//        check();
//        history.kill();
//        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, paths, transition), ViewTransitionDirection.REPLACE, null);
//    }

//    /**
//     * Replace current path with new stack
//     */
//    public void replace(ScreenPathsStack stack) {
//        check();
//        history.kill();
//        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, stack), ViewTransitionDirection.REPLACE, null);
//    }

//    /**
//     * Execute several navigation event on the current stack
//     */
//    public void chain(NavigationChain chain) {
//        chain(chain, null);
//    }


    public boolean back() {
        return back(null);
    }

    public boolean back(Object result) {
        check();
        if (!history.canKill()) {
            return false;
        }

        History.Entry entry = history.kill();
        dispatcher.dispatch(entry, Dispatcher.DISPATCH_BACKWARD, result);

        return true;
    }

    public boolean backToRoot() {
        return back(null);
    }

    public boolean backToRoot(Object result) {
        check();
        if (!history.canKill()) {
            return false;
        }

        dispatcher.dispatch(history.killAllButRoot(), ViewTransition.DIRECTION_BACKWARD, result);
        return true;
    }


//    // REPLACE
//
//    public void replace(ScreenPath screen) {
//        replace(screen, null);
//    }
//
//    public void replace(ScreenPath screen, String transition) {
//        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, screen, transition), ViewTransition.DIRECTION_FORWARD);
//    }
//
//    public void replace(Destination... destination) {
//
//    }
//
//    public void replace(ScreenPath... screens) {
//
//    }
//
//
//    public void navigate(Navigation nav) {
//
//    }


//    /**
//     * Execute several navigation event on the current stack
//     */
//    public void chain(NavigationChain chain, ViewTransitionDirection direction) {
//        check();
//        Preconditions.checkArgument(chain != null && !chain.chains.isEmpty(), "Navigation chain cannot be null nor empty");
//
//        List<History.Entry> entries = new ArrayList<>(chain.chains.size());
//        for (int i = 0; i < chain.chains.size(); i++) {
//            NavigationChain.Chain c = chain.chains.get(i);
//            if (c.path == null) {
//                if (history.canKill()) {
//                    if (c.type == NavigationChain.Chain.TYPE_BACK) {
//                        entries.add(history.kill());
//                    } else {
//                        entries.addAll(history.killAllButRoot());
//                    }
//                }
//            } else {
//                if (c.type == NavigationChain.Chain.TYPE_REPLACE) {
//                    history.kill();
//                }
//
//                // push type for push and replace, modal for show
//                int type = c.type == NavigationChain.Chain.TYPE_PUSH || c.type == NavigationChain.Chain.TYPE_REPLACE ?
//                        History.NAV_TYPE_PUSH : History.NAV_TYPE_MODAL;
//                entries.add(history.add(c.path, type));
//            }
//        }
//
//        dispatcher.dispatch(entries, direction, null);
//    }

//    /**
//     * Set new navigation stack by replacing the current one
//     *
//     * @param direction specify the ViewTransition direction to apply
//     */
//    public void set(ScreenPathsStack stack, ViewTransitionDirection direction) {
//        check();
//
//        List<History.Entry> entries = new ArrayList<>();
//        entries.addAll(history.killAll());
//        entries.addAll(add(History.NAV_TYPE_PUSH, stack));
//        dispatcher.dispatch(entries, direction, null);
//    }

//    public boolean back() {
//        return back(null);
//    }
//
//    public boolean back(Object withResult) {
//        check();
//        if (!history.canKill()) {
//            return false;
//        }
//
//        History.Entry entry = history.kill();
//        dispatcher.dispatch(entry, ViewTransitionDirection.BACKWARD, withResult);
//
//        return true;
//    }
//
//    public boolean backToRoot() {
//        check();
//        if (!history.canKill()) {
//            return false;
//        }
//
//        dispatcher.dispatch(history.killAllButRoot(), ViewTransitionDirection.BACKWARD, null);
//        return true;
//    }

//    private List<History.Entry> add(int navType, ScreenPathsStack stack) {
//        Preconditions.checkArgument(stack != null && !stack.paths.isEmpty(), "Navigation stack cannot be null nor empty");
//        return add(navType, stack.paths.toArray(new ScreenPath[stack.paths.size()]));
//    }

    private List<History.Entry> add(int navType, ScreenPath... paths) {
        Preconditions.checkArgument(paths != null && paths.length > 0, "Screens cannot be null or empty");

        List<History.Entry> entries = new ArrayList<>(paths.length);
        for (int i = 0; i < paths.length; i++) {
            entries.add(history.add(paths[i], navType, null, null));
        }

        return entries;
    }

    private List<History.Entry> add(int navType, PathBuilder... builders) {
        Preconditions.checkArgument(builders != null && builders.length > 0, "Path builders cannot be null or empty");

        List<History.Entry> entries = new ArrayList<>(builders.length);
        PathBuilder builder;
        for (int i = 0; i < builders.length; i++) {
            builder = builders[i];
            entries.add(history.add(builder.path, navType, builder.transition, builder.id));
        }

        return entries;
    }

    private History.Entry add(int navType, ScreenPath path, String transition, String id) {
        Preconditions.checkNotNull(path, "Screen cannot be null");
        return history.add(path, navType, transition, id);
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
}
