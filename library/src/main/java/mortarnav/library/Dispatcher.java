package mortarnav.library;

import java.util.ArrayList;
import java.util.List;

import mortar.MortarScope;

/**
 * Dispatch consequences of history manipulation
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class Dispatcher {

    private final Navigator navigator;
    private boolean dispatching;
    private boolean killed;

    Dispatcher(Navigator navigator) {
        this.navigator = navigator;
    }

    /**
     * Stop the dispatcher forever
     * Its only salvation lies in garbage collection now
     */
    void kill() {
        Preconditions.checkArgument(!killed, "Did you try to kill the dispatcher twice? Not cool brah");
        killed = true;
    }


    void dispatch() {
        if (killed || dispatching || !navigator.presenter.isActive()) return;
        dispatching = true;
        Preconditions.checkNotNull(navigator.getScope(), "Dispatcher navigator scope cannot be null");
        Preconditions.checkArgument(!navigator.history.isEmpty(), "Cannot dispatch empty history");

        final History.Entry top = navigator.history.peekAlive();
        Preconditions.checkNotNull(top, "Cannot peek at least one alive entry from history");
        Preconditions.checkArgument(!top.dead, "Cannot dispatch dead entry of history");
        Logger.d("Peek history scope : %s", top.scopeName);

        // default direction is FORWARD, unless we find previous scope in history
        Direction direction = Direction.FORWARD;

        MortarScope currentScope = navigator.presenter.getCurrentScope();
        if (currentScope != null) {
            if (currentScope.getName().equals(top.scopeName)) {
                // history in sync with current element, work is done
                endDispatch();
                return;
            }

            History.Entry existingEntry = navigator.history.find(currentScope.getName());
            if (existingEntry == null || existingEntry.dead) {
                // a scope already exists for the current view, but not anymore in history
                // destroy it
                // => backward navigation
                direction = Direction.BACKWARD;
            } else {
                // scope still exists in history, save view state
                // => forward navigation
                existingEntry.state = navigator.presenter.getCurrentViewState();
                direction = Direction.FORWARD;
            }
        }

        MortarScope newScope = navigator.getScope().findChild(top.scopeName);
        if (newScope == null) {
            newScope = NavigationScopeFactory.createScope(navigator.getScope(), top.scope, top.scopeName);
        }

        navigator.presenter.present(top, newScope, direction, new Callback() {
            @Override
            public void onComplete() {
                if (navigator.getScope() != null) {
                    clean(top);
                }

                endDispatch();
                dispatch();
            }
        });
    }

    private void clean(final History.Entry current) {
        final List<History.Entry> entriesToRemove = new ArrayList<>();
        navigator.history.filterFromTop(new History.Filter() {
            @Override
            public boolean filter(History.Entry entry) {
                if (entry == current) {
                    // stop when we get to the currently dispatched entry
                    return false;
                }

                if (entry.dead) {
                    // destroy scope and add entry to be removed from history
                    MortarScope scope = navigator.getScope().findChild(entry.scopeName);
                    if (scope != null) {
                        Logger.d("Destroy scope %s", entry.scopeName);
                        scope.destroy();
                    }
                    entriesToRemove.add(entry);
                }

                return true;
            }
        });

        if (!entriesToRemove.isEmpty()) {
            navigator.history.removeAll(entriesToRemove);
        }
    }

    private void endDispatch() {
        Preconditions.checkArgument(dispatching, "Calling endDispatch while not dispatching");
        dispatching = false;
    }

    interface Callback {
        void onComplete();
    }

    enum Direction {
        FORWARD, BACKWARD
    }
}
