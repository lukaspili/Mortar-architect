package architect;

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
    private final List<History.Entry> entries = new ArrayList<>();
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

    void dispatch(List<History.Entry> e) {
        entries.addAll(e);
        startDispatch();
    }

    void dispatch(History.Entry entry) {
        entries.add(entry);
        startDispatch();
    }

    void dispatch() {
        History.Entry entry = navigator.history.getLastAlive();
        if (entry != null) {
            entries.add(entry);
            startDispatch();
        }
    }

    private void startDispatch() {
        if (killed || dispatching || !navigator.presenter.isActive() || entries.isEmpty()) return;
        dispatching = true;
        Preconditions.checkNotNull(navigator.getScope(), "Dispatcher navigator scope cannot be null");
        Preconditions.checkArgument(!navigator.history.isEmpty(), "Cannot dispatch empty history");

        final History.Entry nextEntry = entries.remove(0);
        Preconditions.checkArgument(navigator.history.existInHistory(nextEntry), "Entry does not exist in history");
        Logger.d("Get next entry with scope: %s", nextEntry.scopeName);

        if (nextEntry.dead) {
            Logger.d("Next entry already dead, remove directly");
            removeDeadEntry(nextEntry);
            endDispatch();
            return;
        }

        Direction direction;
        MortarScope currentScope = navigator.presenter.getCurrentScope();
        final History.Entry previousEntry;
        Logger.d("Current container scope is: %s", currentScope != null ? currentScope.getName() : "NULL");
        if (currentScope != null) {
            if (currentScope.getName().equals(nextEntry.scopeName)) {
                Logger.d("Current scope match new entry");
                endDispatch();
                return;
            }

            previousEntry = navigator.history.getAdjacent(nextEntry, currentScope.getName());
            Preconditions.checkNotNull(previousEntry, "Cannot find previous entry");
            if (previousEntry.dead) {
                // a scope already exists for the current view, but not anymore in history
                // destroy it
                // => backward navigation
                direction = Direction.BACKWARD;
            } else {
                // scope still exists in history, save view state
                // => forward navigation
                previousEntry.state = navigator.presenter.getCurrentViewState();
                direction = Direction.FORWARD;
            }
        } else {
            direction = Direction.REPLACE;
            previousEntry = null;
        }

        MortarScope newScope = navigator.getScope().findChild(nextEntry.scopeName);
        if (newScope == null) {
            newScope = StackFactory.createScope(navigator.getScope(), nextEntry.scope, nextEntry.scopeName);
        }

        navigator.presenter.present(new Dispatch(nextEntry, newScope), previousEntry, direction, new Callback() {
            @Override
            public void onComplete() {
                if (previousEntry != null && previousEntry.dead) {
                    removeDeadEntry(previousEntry);
                }

                endDispatch();
                startDispatch();
            }
        });
    }

    private void removeDeadEntry(History.Entry entry) {
        Logger.d("Remove dead entry: %s", entry.scopeName);
        navigator.history.remove(entry);
        if (navigator.getScope() != null) {
            MortarScope scope = navigator.getScope().findChild(entry.scopeName);
            if (scope != null) {
                Logger.d("Destroy scope %s", entry.scopeName);
                scope.destroy();
            }
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
        FORWARD, BACKWARD, REPLACE
    }

    static class Dispatch {
        History.Entry entry;
        MortarScope scope;

        public Dispatch(History.Entry entry, MortarScope scope) {
            Preconditions.checkArgument(entry.scopeName.equals(scope.getName()), "Dispatch entry scope name does not match");
            this.entry = entry;
            this.scope = scope;
        }
    }
}
