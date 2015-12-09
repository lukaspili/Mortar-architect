package architect;

import architect.hook.Hook;
import architect.service.Registration;
import architect.service.Service;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Architect {

    final ArchitectDelegate delegate;
    final History history;
    final Dispatcher dispatcher;
    final Services services;
    final Attachments attachments;
    final Hooks hooks;

    public static Architect create(ScreenParceler parceler) {
        Hooks hooks = new Hooks();
        History history = new History(parceler, hooks);
        Services services = new Services(history);
        return new Architect(new ArchitectDelegate(), history, new Dispatcher(services, history, hooks), services, new Attachments(services), hooks);
    }

    Architect(ArchitectDelegate delegate, History history, Dispatcher dispatcher, Services services, Attachments attachments, Hooks hooks) {
        this.delegate = delegate;
        this.history = history;
        this.dispatcher = dispatcher;
        this.services = services;
        this.attachments = attachments;
        this.hooks = hooks;

        delegate.set(this);
    }

    public void register(String name, Registration registration) {
        services.register(name, registration.createController(new Executor(name, history, dispatcher)), registration.createPresenter(hooks), registration.createDelegate());
    }

    public void addHook(Hook hook) {
        hooks.add(hook);
    }

    public Service getService(String name) {
        return services.get(name);
    }

//    public Service getTopService() {
//        History.Entry entry = history.getTopDispatched();
//        if (entry == null) {
//            return null;
//        }
//
//        return services.get(entry.service);
//    }

    public ArchitectDelegate delegate() {
        return delegate;
    }


    // Parcelable

//    public Architect(Parcel in) {
//
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeSerializable();
//    }
//
//    public static final Parcelable.Creator<Architect> CREATOR = new Parcelable.Creator<Architect>() {
//        public Architect createFromParcel(Parcel in) {
//            return new Architect(in);
//        }
//
//        public Architect[] newArray(int size) {
//            return new Architect[size];
//        }
//    };


    // PUSH

//    /**
//     * Push one path
//     */
//    public void push(Screen path) {
//        check();
//        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, path, null, null), 0);
//    }
//
//    /**
//     * Push one or several paths
//     */
//    public void push(Screen... paths) {
//        check();
//        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, paths), 0);
//    }
//
//    /**
//     * Push one path builder
//     */
//    public void push(PathBuilder builder) {
//        check();
//        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, builder.path, builder.transition, builder.id), 0);
//    }
//
//    /**
//     * Push several path builders
//     */
//    public void push(PathBuilder... builders) {
//        check();
//        dispatcher.dispatch(add(History.NAV_TYPE_PUSH, builders), 0);
//    }


    // SHOW

//    /**
//     * Show one path
//     */
//    public void show(Screen path) {
//        check();
//        dispatcher.dispatch(add(History.NAV_TYPE_MODAL, path, null, null), 0);
//    }


    // REPLACE

//    /**
//     * Replace current path by one another path
//     */
//    public void replace(Screen path) {
//        replace(path, ViewTransition.DIRECTION_FORWARD);
//    }
//
//    /**
//     * Replace current path by one another path
//     */
//    public void replace(Screen path, int viewTransitionDirection) {
//        replace(viewTransitionDirection, path);
//    }
//
//    /**
//     * Replace current path by several another paths
//     */
//    public void replace(Screen... paths) {
//        replace(ViewTransition.DIRECTION_FORWARD, paths);
//    }
//
//    /**
//     * Replace current path by several another paths
//     */
//    public void replace(int viewTransitionDirection, Screen... paths) {
//        checkWithDirection(viewTransitionDirection);
//
//        List<History.Entry> entries = new ArrayList<>();
//        entries.add(history.kill(null, true));
//        entries.addAll(add(History.NAV_TYPE_PUSH, paths));
//        dispatcher.dispatch(entries, viewTransitionDirection);
//    }
//
//    /**
//     * Replace current path by one path builder
//     */
//    public void replace(PathBuilder builder) {
//        replace(builder, ViewTransition.DIRECTION_FORWARD);
//    }
//
//    /**
//     * Replace current path by one path builder
//     */
//    public void replace(PathBuilder builder, int viewTransitionDirection) {
//        replace(viewTransitionDirection, builder);
//    }
//
//    /**
//     * Push several path builders
//     */
//    public void replace(PathBuilder... builders) {
//        replace(ViewTransition.DIRECTION_FORWARD, builders);
//    }
//
//    /**
//     * Push several path builders
//     */
//    public void replace(int viewTransitionDirection, PathBuilder... builders) {
//        checkWithDirection(viewTransitionDirection);
//
//        List<History.Entry> entries = new ArrayList<>();
//        entries.add(history.kill(null, true));
//        entries.addAll(add(History.NAV_TYPE_PUSH, builders));
//        dispatcher.dispatch(entries, viewTransitionDirection);
//    }


    // BACK

//    public boolean back() {
//        return back(null);
//    }
//
//    public boolean back(Object result) {
//        check();
//        if (!history.canKill()) {
//            return false;
//        }
//
//        dispatcher.dispatch(history.kill(result, false));
//        return true;
//    }
//
//    public boolean backToRoot() {
//        return backToRoot(null);
//    }
//
//    public boolean backToRoot(Object result) {
//        check();
//        if (!history.canKill()) {
//            return false;
//        }
//
//        dispatcher.dispatch(history.killAllButRoot(result));
//        return true;
//    }


    // SET

//    /**
//     * Set new navigation stack by replacing the current one
//     */
//    public void set(Screen... paths) {
//        set(ViewTransition.DIRECTION_FORWARD, paths);
//    }
//
//    /**
//     * Set new navigation stack by replacing the current one
//     */
//    public void set(int viewTransitionDirection, Screen... paths) {
//        checkWithDirection(viewTransitionDirection);
//        List<History.Entry> entries = history.killAll();
//        entries.addAll(add(History.NAV_TYPE_PUSH, paths));
//        dispatcher.dispatch(entries, viewTransitionDirection);
//    }
//
//    /**
//     * Set new navigation stack by replacing the current one
//     */
//    public void set(PathBuilder... builders) {
//        set(ViewTransition.DIRECTION_FORWARD, builders);
//    }
//
//    /**
//     * Set new navigation stack by replacing the current one
//     */
//    public void set(int viewTransitionDirection, PathBuilder... builders) {
//        checkWithDirection(viewTransitionDirection);
//        List<History.Entry> entries = history.killAll();
//        entries.addAll(add(History.NAV_TYPE_PUSH, builders));
//        dispatcher.dispatch(entries, viewTransitionDirection);
//    }


    // NAVIGATE

//    public void navigate(Navigation nav) {
//        navigate(nav, ViewTransition.DIRECTION_FORWARD);
//    }
//
//    public void navigate(Navigation nav, int viewTransitionDirection) {
//        checkWithDirection(viewTransitionDirection);
//        Preconditions.checkArgument(nav != null && !nav.steps.isEmpty(), "Navigation cannot be null nor empty");
//
//        List<History.Entry> entries = new ArrayList<>(nav.steps.size());
//        for (int i = 0; i < nav.steps.size(); i++) {
//            Navigation.Step step = nav.steps.get(i);
//            if (step.path == null && step.builder == null) {
//                if (history.canKill()) {
//                    if (step.type == Navigation.TYPE_BACK) {
//                        entries.add(history.kill(step.result, false));
//                    } else {
//                        entries.addAll(history.killAllButRoot(step.result));
//                    }
//                }
//            } else {
//                if (step.type == Navigation.TYPE_REPLACE) {
//                    if (!history.canReplace()) {
//                        continue;
//                    }
//
//                    entries.add(history.kill(null, true));
//                }
//
//                // push type for push and replace, modal for show
//                int type = step.type == Navigation.TYPE_PUSH || step.type == Navigation.TYPE_REPLACE ?
//                        History.NAV_TYPE_PUSH : History.NAV_TYPE_MODAL;
//                if (step.path != null) {
//                    entries.add(history.add(step.path, type, null, null));
//                } else {
//                    entries.add(history.add(step.builder.path, type, step.builder.transition, step.builder.id));
//                }
//            }
//        }
//
//        dispatcher.dispatch(entries, viewTransitionDirection);
//    }
//
//    private List<History.Entry> add(int navType, Screen... paths) {
//        Preconditions.checkArgument(paths != null && paths.length > 0, "Screens cannot be null or empty");
//
//        List<History.Entry> entries = new ArrayList<>(paths.length);
//        for (int i = 0; i < paths.length; i++) {
//            entries.add(history.add(paths[i], navType, null, null));
//        }
//
//        return entries;
//    }
//
//    private List<History.Entry> add(int navType, PathBuilder... builders) {
//        Preconditions.checkArgument(builders != null && builders.length > 0, "Path builders cannot be null or empty");
//
//        List<History.Entry> entries = new ArrayList<>(builders.length);
//        PathBuilder builder;
//        for (int i = 0; i < builders.length; i++) {
//            builder = builders[i];
//            entries.add(history.add(builder.path, navType, builder.transition, builder.id));
//        }
//
//        return entries;
//    }

//    private History.Entry add(Screen screen, String service, String transition, String tag) {
//        return ;
//    }

//    private void check() {
//        Preconditions.checkNotNull(scope, "Navigator scope cannot be null");
//    }

//    private void checkWithDirection(int transitionDirection) {
//        check();
//        Preconditions.checkArgument(history.canReplace(), "No path to replace");
//        Preconditions.checkArgument(transitionDirection == ViewTransition.DIRECTION_FORWARD || transitionDirection == ViewTransition.DIRECTION_BACKWARD, "View transition direction invalid value, must be either ViewTransition.DIRECTION_FORWARD or ViewTransition.DIRECTION_BACKWARD");
//    }


//    /**
//     * Scope can be null if the method is called after the navigator scope was destroyed
//     * //TODO: is it really possible to be null?
//     */
//    MortarScope getScope() {
//        return scope;
//    }


//    public Transitions transitions() {
//        return transitions;
//    }


    // Scoped

//    @Override
//    public void onEnterScope(MortarScope scope) {
//        Preconditions.checkNull(this.scope, "Cannot register navigator multiple times in a scope");
//        this.scope = scope;
//    }
//
//    /**
//     * Scope associated to navigator is destroyed
//     * Everything will be destroyed
//     */
//    @Override
//    public void onExitScope() {
//        Logger.d("Navigation scope exit");
//
//        // stop and kill the dispatcher
//        dispatcher.kill();
//
//        scope = null;
//    }
}
