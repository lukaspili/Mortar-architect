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
    private boolean active;

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

    /**
     * Attach the dispatcher on new context
     */
    void activate() {
        Preconditions.checkArgument(!active, "Dispatcher already active");
        Preconditions.checkArgument(entries.isEmpty(), "Dispatcher stack must be empty");
        Preconditions.checkNotNull(navigator.getScope(), "Navigator scope cannot be null");

        // clean dead entries that may had happen on history while dispatcher
        List<History.Entry> dead = navigator.history.removeAllDead();
        if (dead != null && !dead.isEmpty()) {
            History.Entry entry;
            for (int i = 0; i < dead.size(); i++) {
                entry = dead.get(i);
                Logger.d("Dead entry: %s", entry.scopeName);
                MortarScope scope = navigator.getScope().findChild(entry.scopeName);
                if (scope != null) {
                    Logger.d("Clean and destroy scope %s", entry.scopeName);
                    scope.destroy();
                }
            }
        }

        History.Entry entry = navigator.history.getLastAlive();
        Preconditions.checkNotNull(entry, "No alive entry");
        Logger.d("Last alive entry: %s", entry.scopeName);

        MortarScope entryScope = navigator.getScope().findChild(entry.scopeName);
        if (entryScope == null) {
            entryScope = StackFactory.createScope(navigator.getScope(), entry.scope, entry.scopeName);
        }

        List<Dispatch> dispatches = null;
        if (entry.isModal()) {
            // entry modal, get previous displayed
            List<History.Entry> previous = navigator.history.getPreviousOfModal(entry);
            if (previous != null && !previous.isEmpty()) {
                dispatches = new ArrayList<>(previous.size() + 1);
                History.Entry prevEntry;
                MortarScope scope;
                for (int i = previous.size() - 1; i >= 0; i--) {
                    prevEntry = previous.get(i);
                    Logger.d("Get previous entry: %s", prevEntry.scopeName);
                    scope = navigator.getScope().findChild(prevEntry.scopeName);
                    if (scope == null) {
                        scope = StackFactory.createScope(navigator.getScope(), prevEntry.scope, prevEntry.scopeName);
                    }
                    dispatches.add(new Dispatch(prevEntry, scope));
                }
            }
        }

        if (dispatches == null) {
            dispatches = new ArrayList<>(1);
        }
        dispatches.add(new Dispatch(entry, entryScope));

        navigator.presenter.restore(dispatches);
        active = true;
    }

    void desactivate() {
        Preconditions.checkArgument(active, "Dispatcher already desactivated");
        active = false;
        entries.clear();
    }

    void dispatch(List<History.Entry> e) {
        if (!active) return;

        entries.addAll(e);
        startDispatch();
    }

    void dispatch(History.Entry entry) {
        if (!active) return;

        entries.add(entry);
        startDispatch();
    }

    void startDispatch() {
        Preconditions.checkArgument(active, "Dispatcher must be active");

        if (killed || dispatching || !navigator.presenter.isActive() || entries.isEmpty()) return;
        dispatching = true;
        Preconditions.checkNotNull(navigator.getScope(), "Dispatcher navigator scope cannot be null");
        Preconditions.checkArgument(!navigator.history.isEmpty(), "Cannot dispatch on empty history");
        Preconditions.checkArgument(!entries.isEmpty(), "Cannot dispatch on empty stack");

        final History.Entry entry = entries.remove(0);
        Preconditions.checkArgument(navigator.history.existInHistory(entry), "Entry does not exist in history");
        Logger.d("Get entry with scope: %s", entry.scopeName);

        MortarScope currentScope = navigator.presenter.getCurrentScope();
        Preconditions.checkNotNull(currentScope, "Current scope cannot be null");
        Logger.d("Current container scope is: %s", currentScope.getName());

        Direction direction;
        final History.Entry nextEntry;
        final History.Entry previousEntry;
        if (entry.dead) {
            direction = Direction.BACKWARD;
            previousEntry = entry;
            nextEntry = navigator.history.getLeftOf(entry);
        } else {
            direction = Direction.FORWARD;
            nextEntry = entry;
            previousEntry = navigator.history.getLeftOf(entry);
        }

        Preconditions.checkNotNull(nextEntry, "Next entry cannot be null");
        Preconditions.checkNotNull(previousEntry, "Previous entry cannot be null");
        Preconditions.checkArgument(previousEntry.scopeName.equals(currentScope.getName()), "Current scope name must match the previous entry scope name");

        MortarScope nextScope = navigator.getScope().findChild(nextEntry.scopeName);
        if (nextScope == null) {
            nextScope = StackFactory.createScope(navigator.getScope(), nextEntry.scope, nextEntry.scopeName);
        }

        navigator.presenter.present(new Dispatch(nextEntry, nextScope), previousEntry, direction, new Callback() {
            @Override
            public void onComplete() {
                if (previousEntry.dead) {
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
        FORWARD, BACKWARD
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
