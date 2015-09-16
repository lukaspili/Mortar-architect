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
        // was inactive
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
            entryScope = StackFactory.createScope(navigator.getScope(), entry.path, entry.scopeName);
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
                        scope = StackFactory.createScope(navigator.getScope(), prevEntry.path, prevEntry.scopeName);
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

        ViewTransitionDirection direction;
        History.Entry nextEntry;
        final History.Entry previousEntry;
        if (entry.dead) {
            direction = ViewTransitionDirection.BACKWARD;
            previousEntry = entry;
            nextEntry = navigator.history.getLeftOf(entry);
        } else {
            direction = ViewTransitionDirection.FORWARD;
            nextEntry = entry;
            previousEntry = navigator.history.getLeftOf(entry);
        }

        Preconditions.checkNotNull(nextEntry, "Next entry cannot be null");
        Preconditions.checkNotNull(previousEntry, "Previous entry cannot be null");
        Preconditions.checkNull(nextEntry.receivedResult, "Next entry cannot have already a result");

        if (!entries.isEmpty()) {
            boolean fastForwarded = fastForward(entry, previousEntry);
            if (fastForwarded) {
                return;
            }
        }

        if (entry.dead && previousEntry.returnsResult != null) {
            nextEntry.receivedResult = previousEntry.returnsResult;
            previousEntry.returnsResult = null;
        }

        present(nextEntry, previousEntry, direction);
    }

    private void present(History.Entry nextEntry, final History.Entry previousEntry, ViewTransitionDirection direction) {
        MortarScope currentScope = navigator.presenter.getCurrentScope();
        Preconditions.checkNotNull(currentScope, "Current scope cannot be null");
        Logger.d("Current container scope is: %s", currentScope.getName());

        navigator.presenter.present(createDispatch(nextEntry), previousEntry, direction, new Callback() {
            @Override
            public void onComplete() {
                if (previousEntry.dead) {
                    destroyDead(previousEntry);
                }

                endDispatch();
                startDispatch();
            }
        });
    }

    private boolean fastForward(History.Entry entry, History.Entry previousEntry) {
        boolean fastForwarded = false;
        List<Dispatch> modals = null;
        History.Entry nextDispatch = null;
        while (!entries.isEmpty() && (nextDispatch = entries.get(0)) != null) {
            Logger.d("Get next dispatch: %s", nextDispatch.scopeName);

            if (entry.isModal()) {
                // fast forward for modals works only with other modals
                // it will animate all the fast-forwarded modals at once (in parallel)
                if (!nextDispatch.isModal()) {
                    break;
                }

                if (modals == null) {
                    modals = new ArrayList<>();
                    modals.add(createDispatch(entry));
                }

                fastForwarded = true;
                entries.remove(0);
                modals.add(createDispatch(nextDispatch));

                Logger.d("Modal fast forward to next entry: %s", nextDispatch.scopeName);
                continue;
            }

            // non-modal fast-forward
            if (nextDispatch.isModal()) {
                // fast forward only on non-modals
                break;
            }

            fastForwarded = true;
            entries.remove(0);

            if (nextDispatch.dead) {
                History.Entry toDestroy = nextDispatch;
                nextDispatch = navigator.history.getLeftOf(nextDispatch);
                destroyDead(toDestroy);
            }

            Logger.d("Fast forward to next entry: %s", nextDispatch.scopeName);
        }

        if (!fastForwarded) {
            return false;
        }

        if (entry.dead && previousEntry.returnsResult != null) {
            nextDispatch.receivedResult = previousEntry.returnsResult;
            previousEntry.returnsResult = null;
            Logger.d("Pass result from %s to %s", previousEntry.scopeName, nextDispatch.scopeName);
        }

        if (entry.isModal()) {
            final List<Dispatch> finalModals = modals;
            navigator.presenter.presentModals(modals, new Callback() {
                @Override
                public void onComplete() {
                    Dispatch dispatch;
                    for (int i = 0; i < finalModals.size(); i++) {
                        dispatch = finalModals.get(i);
                        if (dispatch.entry.dead) {
                            destroyDead(dispatch.entry);
                        }
                    }

                    endDispatch();
                    startDispatch();
                }
            });
        } else {
            ViewTransitionDirection direction;
            if (nextDispatch.direction != null) {
                direction = nextDispatch.direction;
                nextDispatch.direction = null;
            } else {
                direction = previousEntry.dead ? ViewTransitionDirection.BACKWARD : ViewTransitionDirection.FORWARD;
            }
            present(nextDispatch, previousEntry, direction);
        }

        return true;
    }

    private Dispatch createDispatch(History.Entry entry) {
        MortarScope nextScope = navigator.getScope().findChild(entry.scopeName);
        if (nextScope == null) {
            nextScope = StackFactory.createScope(navigator.getScope(), entry.path, entry.scopeName);
        }

        return new Dispatch(entry, nextScope);
    }

    private void destroyDead(History.Entry entry) {
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

//    enum Direction {
//        FORWARD, BACKWARD
//    }

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
