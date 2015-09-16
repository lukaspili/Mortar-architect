package architect;

import java.util.ArrayList;
import java.util.List;

import architect.screen.ReceivesNavigationResult;
import mortar.MortarScope;

/**
 * Dispatch consequences of history manipulation
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class Dispatcher {

    private final Navigator navigator;
    private final List<History.Entry> entries = new ArrayList<>();
    private ViewTransitionDirection nextEntryTransitionDirection;
    private Object nextEntryResult;
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

        History.Entry entry = navigator.history.getLast();
        Preconditions.checkNotNull(entry, "No entry");
        Logger.d("Activate last: %s", entry.scopeName);

        List<DispatchEntry> dispatchEntries;
        if (entry.isModal()) {
            List<History.Entry> previous = navigator.history.getLastWithModals();
            Preconditions.checkArgument(previous.get(previous.size() - 1) == entry, "Entry mismatch");
            dispatchEntries = new ArrayList<>(previous.size());

            History.Entry prevEntry;
            MortarScope scope;
            for (int i = previous.size() - 1; i >= 0; i--) {
                prevEntry = previous.get(i);
                Logger.d("Get previous entry: %s", prevEntry.scopeName);
                scope = navigator.getScope().findChild(prevEntry.scopeName);
                dispatchEntries.add(new DispatchEntry(prevEntry, scope));
            }
        } else {
            dispatchEntries = new ArrayList<>(1);
            MortarScope entryScope = navigator.getScope().findChild(entry.scopeName);
            if (entryScope == null) {
                // entry scope is null during the first launch
                Logger.d("Create scope: %s", entry.scopeName);
                entryScope = StackFactory.createScope(navigator.getScope(), entry.path, entry.scopeName);
            }
            dispatchEntries.add(new DispatchEntry(entry, entryScope));
        }

        navigator.presenter.restore(dispatchEntries);
        active = true;


//        // clean dead entries that may had happen on history while dispatcher
//        // was inactive
//        List<History.Entry> dead = navigator.history.removeAllDead();
//        if (dead != null && !dead.isEmpty()) {
//            History.Entry entry;
//            for (int i = 0; i < dead.size(); i++) {
//                entry = dead.get(i);
//                Logger.d("Dead entry: %s", entry.scopeName);
//                MortarScope scope = navigator.getScope().findChild(entry.scopeName);
//                if (scope != null) {
//                    Logger.d("Clean and destroy scope %s", entry.scopeName);
//                    scope.destroy();
//                }
//            }
//        }


//        History.Entry entry = navigator.history.getLastAlive();
//        Preconditions.checkNotNull(entry, "No alive entry");
//        Logger.d("Last alive entry: %s", entry.path);
//
//        MortarScope entryScope = navigator.getScope().findChild(entry.scopeName);
//        if (entryScope == null) {
//            entryScope = StackFactory.createScope(navigator.getScope(), entry.path, entry.scopeName);
//        }
//
//        List<Dispatch> dispatches = null;
//        if (entry.isModal()) {
//            // entry modal, get previous displayed
//            List<History.Entry> previous = navigator.history.getPreviousOfModal(entry);
//            if (previous != null && !previous.isEmpty()) {
//                dispatches = new ArrayList<>(previous.size() + 1);
//                History.Entry prevEntry;
//                MortarScope scope;
//                for (int i = previous.size() - 1; i >= 0; i--) {
//                    prevEntry = previous.get(i);
//                    Logger.d("Get previous entry: %s", prevEntry.scopeName);
//                    scope = navigator.getScope().findChild(prevEntry.scopeName);
//                    if (scope == null) {
//                        scope = StackFactory.createScope(navigator.getScope(), prevEntry.path, prevEntry.scopeName);
//                    }
//                    dispatches.add(new Dispatch(prevEntry, scope));
//                }
//            }
//        }
//
//        if (dispatches == null) {
//            dispatches = new ArrayList<>(1);
//        }
//        dispatches.add(new Dispatch(entry, entryScope));
//
//        navigator.presenter.restore(dispatches);
//        active = true;
    }

    void desactivate() {
        Preconditions.checkArgument(active, "Dispatcher already desactivated");
        active = false;
        entries.clear();
    }

    void dispatch(List<History.Entry> e, ViewTransitionDirection nextEntryTransitionDirection, Object nextEntryResult) {
        if (!active) return;

        entries.addAll(e);
        this.nextEntryTransitionDirection = nextEntryTransitionDirection;
        this.nextEntryResult = nextEntryResult;
        startDispatch();
    }

    void dispatch(History.Entry entry, ViewTransitionDirection nextEntryTransitionDirection, Object nextEntryResult) {
        if (!active) return;

        entries.add(entry);
        this.nextEntryTransitionDirection = nextEntryTransitionDirection;
        this.nextEntryResult = nextEntryResult;
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
        Logger.d("Entry to dispatch: %s", entry.scopeName);

        final boolean inHistory = navigator.history.existInHistory(entry);
        final History.Entry enterEntry;
        final History.Entry exitEntry;
        if (inHistory) {
            // new entry, we go to this one, from previous one
            enterEntry = entry;
            exitEntry = navigator.history.getLeftOf(entry);
        } else {
            // entry is dead, we go to previous one, from this one
            exitEntry = entry;
            enterEntry = navigator.history.getLast();
        }

        Logger.d("Exit entry: %s - in history: %b", exitEntry.scopeName, inHistory);
        Preconditions.checkNotNull(enterEntry, "Next entry cannot be null");
        Preconditions.checkNotNull(exitEntry, "Previous entry cannot be null");
//        Preconditions.checkNull(nextEntry.receivedResult, "Next entry cannot have already a result");

        if (nextEntryResult != null) {
            Preconditions.checkArgument(inHistory, "Next entry result only if entry is dead");
            if (enterEntry.path instanceof ReceivesNavigationResult) {
                ((ReceivesNavigationResult) enterEntry.path).onReceiveNavigationResult(nextEntryResult);
            }
            nextEntryResult = null;
        }

//        if (!entries.isEmpty()) {
//            // more entries to dispatch, try to skip intermediate entries
//            boolean fastForwarded = fastForward(entry, exitEntry);
//            if (fastForwarded) {
//                return;
//            }
//        }
//
//        if (entry.dead && previousEntry.returnsResult != null) {
//            nextEntry.receivedResult = previousEntry.returnsResult;
//            previousEntry.returnsResult = null;
//        }

        present(enterEntry, exitEntry, inHistory);
    }

    private void present(final History.Entry enterEntry, final History.Entry exitEntry, final boolean exitEntryInHistory) {
//        MortarScope currentScope = navigator.presenter.getCurrentScope();
//        Preconditions.checkNotNull(currentScope, "Current scope cannot be null");
//        Logger.d("Current container scope is: %s", currentScope.getName());

        navigator.presenter.present(createDispatchEntry(enterEntry), exitEntry, nextEntryTransitionDirection, exitEntryInHistory, new Callback() {
            @Override
            public void onComplete() {
                if (navigator.getScope() != null) {
                    Logger.d("Destroy scope: %s", exitEntry.scopeName);
                    navigator.getScope().findChild(exitEntry.scopeName).destroy();
                }

                endDispatch();
                startDispatch(); // maybe something else to dispatch
            }
        });
    }

//    private boolean fastForward(History.Entry entry, History.Entry previousEntry) {
//        boolean fastForwarded = false;
//        List<DispatchEntry> modals = null;
//        History.Entry nextDispatch = null;
//        while (!entries.isEmpty() && (nextDispatch = entries.get(0)) != null) {
//            Logger.d("Get next dispatch: %s", nextDispatch.path);
//
//            if (entry.isModal()) {
//                // fast forward for modals works only with other modals
//                // it will animate all the fast-forwarded modals at once (in parallel)
//                if (!nextDispatch.isModal()) {
//                    break;
//                }
//
//                if (modals == null) {
//                    modals = new ArrayList<>();
//                    modals.add(createDispatchEntry(entry));
//                }
//
//                fastForwarded = true;
//                entries.remove(0);
//                modals.add(createDispatchEntry(nextDispatch));
//
//                Logger.d("Modal fast forward to next entry: %s", nextDispatch.path);
//                continue;
//            }
//
//            // non-modal fast-forward
//            if (nextDispatch.isModal()) {
//                // fast forward only on non-modals
//                break;
//            }
//
//            fastForwarded = true;
//            entries.remove(0);
//
//            if (nextDispatch.dead) {
//                History.Entry toDestroy = nextDispatch;
//                nextDispatch = navigator.history.getLeftOf(nextDispatch);
//                cleanExit(toDestroy);
//            }
//
//            Logger.d("Fast forward to next entry: %s", nextDispatch.path);
//        }
//
//        if (!fastForwarded) {
//            return false;
//        }
//
//        if (entry.isModal()) {
//            final List<DispatchEntry> finalModals = modals;
//            navigator.presenter.presentModals(modals, new Callback() {
//                @Override
//                public void onComplete() {
//                    DispatchEntry dispatchEntry;
//                    for (int i = 0; i < finalModals.size(); i++) {
//                        dispatchEntry = finalModals.get(i);
//                        if (dispatchEntry.entry.dead) {
//                            cleanExit(dispatchEntry.entry);
//                        }
//                    }
//
//                    endDispatch();
//                    startDispatch();
//                }
//            });
//        } else {
////            ViewTransitionDirection direction;
////            if (nextDispatch.direction != null) {
////                direction = nextDispatch.direction;
////                nextDispatch.direction = null;
////            } else {
////                direction = previousEntry.dead ? ViewTransitionDirection.BACKWARD : ViewTransitionDirection.FORWARD;
////            }
//            present(nextDispatch, previousEntry);
//        }
//
//        return true;
//    }

    private DispatchEntry createDispatchEntry(History.Entry entry) {
        return new DispatchEntry(entry, StackFactory.createScope(navigator.getScope(), entry.path, entry.scopeName));
    }

    private void cleanExit(History.Entry entry) {

    }

    private void endDispatch() {
        Preconditions.checkArgument(dispatching, "Calling endDispatch while not dispatching");
        dispatching = false;
    }

    interface Callback {
        void onComplete();
    }

    static class DispatchEntry {
        final History.Entry entry;
        final MortarScope scope;

        public DispatchEntry(History.Entry entry, MortarScope scope) {
            Preconditions.checkNotNull(entry, "Entry null");
            Preconditions.checkNotNull(scope, "Scope null");
            Preconditions.checkArgument(entry.scopeName.equals(scope.getName()), "Scope name mismatch");
            this.entry = entry;
            this.scope = scope;
        }
    }
}
