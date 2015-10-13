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
//    public static Navigator find(Context context) {
//        MortarScope scope = MortarScope.findChild(context, SCOPE_NAME);
//        return scope != null ? scope.<Navigator>getService(SERVICE_NAME) : null;
//    }

//    public static Navigator create(MortarScope containerScope, StackableParceler parceler) {
//        return create(containerScope, parceler, null);
//    }

//    public static Navigator create(MortarScope containerScope, ScreenParceler parceler) {
//        Preconditions.checkNotNull(containerScope, "Mortar scope for Navigator cannot be null");
//        Preconditions.checkArgument(parceler != null, "Parceler for Navigator cannot be null");
//
//        Navigator navigator = new Navigator(parceler);
//
//        MortarScope scope = containerScope.buildChild()
//                .withService(SERVICE_NAME, navigator)
//                .build(SCOPE_NAME);
//        scope.register(navigator);
//
//        return navigator;
//    }

    //    final Config config;
    final History history;
    final Transitions transitions;
    final Presenter presenter;
    final NavigatorLifecycleDelegate delegate;
    final Scoper scoper;
    final Dispatcher dispatcher;
    private MortarScope scope;

    public Navigator(ScreenParceler parceler) {
        history = new History(parceler);
        transitions = new Transitions();
        delegate = new NavigatorLifecycleDelegate(this);
        scoper = new Scoper(this);
        dispatcher = new Dispatcher(this);
        presenter = new Presenter(transitions);
    }


    // PUSH

    /**
     * Push one path
     */
    public void push(ScreenPath path) {
        check();
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, path, null, null), 0);
    }

    /**
     * Push one or several paths
     */
    public void push(ScreenPath... paths) {
        check();
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, paths), 0);
    }

    /**
     * Push one path builder
     */
    public void push(PathBuilder builder) {
        check();
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, builder.path, builder.transition, builder.id), 0);
    }

    /**
     * Push several path builders
     */
    public void push(PathBuilder... builders) {
        check();
        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, builders), 0);
    }


    // REPLACE

    /**
     * Replace current path by one another path
     */
    public void replace(ScreenPath path) {
        replace(path, ViewTransition.DIRECTION_FORWARD);
    }

    /**
     * Replace current path by one another path
     */
    public void replace(ScreenPath path, int viewTransitionDirection) {
        replace(viewTransitionDirection, path);
    }

    /**
     * Replace current path by several another paths
     */
    public void replace(ScreenPath... paths) {
        replace(ViewTransition.DIRECTION_FORWARD, paths);
    }

    /**
     * Replace current path by several another paths
     */
    public void replace(int viewTransitionDirection, ScreenPath... paths) {
        checkWithDirection(viewTransitionDirection);

        List<History.Entry> entries = new ArrayList<>();
        entries.add(history.kill(null));
        entries.addAll(add(History.NAV_TYPE_PUSH, paths));
        dispatcher.dispatch(entries, viewTransitionDirection);
    }

    /**
     * Replace current path by one path builder
     */
    public void replace(PathBuilder builder) {
        replace(builder, ViewTransition.DIRECTION_FORWARD);
    }

    /**
     * Replace current path by one path builder
     */
    public void replace(PathBuilder builder, int viewTransitionDirection) {
        replace(viewTransitionDirection, builder);
    }

    /**
     * Push several path builders
     */
    public void replace(PathBuilder... builders) {
        replace(ViewTransition.DIRECTION_FORWARD, builders);
    }

    /**
     * Push several path builders
     */
    public void replace(int viewTransitionDirection, PathBuilder... builders) {
        checkWithDirection(viewTransitionDirection);

        List<History.Entry> entries = new ArrayList<>();
        entries.add(history.kill(null));
        entries.addAll(add(History.NAV_TYPE_PUSH, builders));
        dispatcher.dispatch(entries, viewTransitionDirection);
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


    // BACK

    public boolean back() {
        return back(null);
    }

    public boolean back(Object result) {
        check();
        if (!history.canKill()) {
            return false;
        }

        dispatcher.dispatch(history.kill(result));
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

        dispatcher.dispatch(history.killAllButRoot(result));
        return true;
    }


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


    // SET

    /**
     * Set new navigation stack by replacing the current one
     */
    public void set(ScreenPath... paths) {
        set(ViewTransition.DIRECTION_FORWARD, paths);
    }

    /**
     * Set new navigation stack by replacing the current one
     */
    public void set(int viewTransitionDirection, ScreenPath... paths) {
        checkWithDirection(viewTransitionDirection);
        List<History.Entry> entries = history.killAll();
        entries.addAll(add(History.NAV_TYPE_PUSH, paths));
        dispatcher.dispatch(entries, viewTransitionDirection);
    }

    /**
     * Set new navigation stack by replacing the current one
     */
    public void set(PathBuilder... builders) {
        set(ViewTransition.DIRECTION_FORWARD, builders);
    }

    /**
     * Set new navigation stack by replacing the current one
     */
    public void set(int viewTransitionDirection, PathBuilder... builders) {
        checkWithDirection(viewTransitionDirection);
        List<History.Entry> entries = history.killAll();
        entries.addAll(add(History.NAV_TYPE_PUSH, builders));
        dispatcher.dispatch(entries, viewTransitionDirection);
    }


    // NAVIGATE

    public void navigate(Navigation nav) {
        navigate(nav, ViewTransition.DIRECTION_FORWARD);
    }

    public void navigate(Navigation nav, int viewTransitionDirection) {
        checkWithDirection(viewTransitionDirection);
        Preconditions.checkArgument(nav != null && !nav.steps.isEmpty(), "Navigation cannot be null nor empty");

        List<History.Entry> entries = new ArrayList<>(nav.steps.size());
        for (int i = 0; i < nav.steps.size(); i++) {
            Navigation.Step step = nav.steps.get(i);
            if (step.path == null && step.builder == null) {
                if (history.canKill()) {
                    if (step.type == Navigation.TYPE_BACK) {
                        entries.add(history.kill(step.result));
                    } else {
                        entries.addAll(history.killAllButRoot(step.result));
                    }
                }
            } else {
                if (step.type == Navigation.TYPE_REPLACE) {
                    if (!history.canReplace()) {
                        continue;
                    }

                    entries.add(history.kill(null));
                }

                // push type for push and replace, modal for show
                int type = step.type == Navigation.TYPE_PUSH || step.type == Navigation.TYPE_REPLACE ?
                        History.NAV_TYPE_PUSH : History.NAV_TYPE_MODAL;
                if (step.path != null) {
                    entries.add(history.add(step.path, type, null, null));
                } else {
                    entries.add(history.add(step.builder.path, type, step.builder.transition, step.builder.id));
                }
            }
        }

        dispatcher.dispatch(entries, viewTransitionDirection);
    }

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

    private void checkWithDirection(int transitionDirection) {
        check();
        Preconditions.checkArgument(history.canReplace(), "No path to replace");
        Preconditions.checkArgument(transitionDirection == ViewTransition.DIRECTION_FORWARD || transitionDirection == ViewTransition.DIRECTION_BACKWARD, "View transition direction invalid value, must be either ViewTransition.DIRECTION_FORWARD or ViewTransition.DIRECTION_BACKWARD");
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
