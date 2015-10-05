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

//    static final int DISPATCH_FORWARD = 1;
//    static final int DISPATCH_BACKWARD = 2;

    private final Navigator navigator;
    private final List<Dispatch> entries = new ArrayList<>();

//    /**
//     * The transition direction of the next dispatch
//     */
//    private int dispatchDirection;

//    /**
//     * The result to pass to the next entry screen
//     */
//    private Object dispatchResult;

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
        Logger.d("Activate last: %s", entry);

        List<ScopedEntry> dispatchedEntries;
        if (entry.isModal()) {
            List<History.Entry> previous = navigator.history.getLastWithModals();
            Preconditions.checkArgument(previous.get(previous.size() - 1) == entry, "Entry mismatch");
            dispatchedEntries = new ArrayList<>(previous.size());

            History.Entry prevEntry;
            MortarScope scope;
            for (int i = previous.size() - 1; i >= 0; i--) {
                prevEntry = previous.get(i);
                Logger.d("Get previous entry: %s", prevEntry);
                throw new RuntimeException("fixme");//TODO yo
//                scope = navigator.getScope().findChild(prevEntry.scopeName);
//                dispatchedEntries.add(new DispatchedEntry(prevEntry, scope));
            }
        } else {
            dispatchedEntries = new ArrayList<>(1);
            dispatchedEntries.add(new ScopedEntry(entry, navigator.scoper.getCurrentScope(entry.path)));
        }

        navigator.presenter.restore(dispatchedEntries);
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

    void dispatch(List<History.Entry> e) {
        dispatch(e, null, 0);
    }

    void dispatch(List<History.Entry> e, Object result) {
        dispatch(e, result, 0);
    }

    void dispatch(List<History.Entry> e, Object result, int direction) {
        if (!active) return;

        for (int i = 0; i < e.size(); i++) {
            entries.add(new Dispatch(e.get(i), result, direction));
        }
        startDispatch();
    }

    void dispatch(History.Entry entry) {
        dispatch(entry, null, 0);
    }

    void dispatch(History.Entry entry, Object result) {
        dispatch(entry, result, 0);
    }

    void dispatch(History.Entry entry, Object result, int direction) {
        if (!active) return;

        entries.add(new Dispatch(entry, result, direction));
        startDispatch();
    }

    void startDispatch() {
        Preconditions.checkArgument(active, "Dispatcher must be active");

        if (killed || dispatching || !navigator.presenter.isActive() || entries.isEmpty()) return;
        dispatching = true;
        Preconditions.checkNotNull(navigator.getScope(), "Dispatcher navigator scope cannot be null");
        Preconditions.checkArgument(!navigator.history.isEmpty(), "Cannot dispatch on empty history");
        Preconditions.checkArgument(!entries.isEmpty(), "Cannot dispatch on empty stack");
//        Preconditions.checkArgument(dispatchDirection > 0, "Dispatch direction invalid value");

        final Dispatch dispatch = entries.remove(0);
        Logger.d("Entry to dispatch: %s", dispatch.entry);

        // in case of forward, the entry to dispatch is already added to history
        // in opposite in case of backward, the entry to dispatch is not in history anymore
        final boolean forward = navigator.history.existInHistory(dispatch.entry);
        final History.Entry enterEntry;
        final History.Entry exitEntry;
        if (forward) {
            // new entry, we go to this one, from previous one
            enterEntry = dispatch.entry;
            exitEntry = navigator.history.getLeftOf(dispatch.entry);
        } else {
            // entry is dead, we go to previous one, from this one
            exitEntry = dispatch.entry;
            enterEntry = navigator.history.getLast();
        }

        Logger.d("Exit entry: %s - in history: %b", exitEntry, forward);
        Preconditions.checkNotNull(enterEntry, "Next entry cannot be null");
        Preconditions.checkNotNull(exitEntry, "Previous entry cannot be null");
        Preconditions.checkArgument(!forward || dispatch.result == null, "In forward dispatch, there is no result");

        if (dispatch.result != null) {
            if (enterEntry.path instanceof ReceivesNavigationResult) {
                ((ReceivesNavigationResult) enterEntry.path).onReceiveNavigationResult(dispatch.result);
            }
        }

//        if (enterEntry.navigationResult != null) {
//            Preconditions.checkArgument(!forward, "Enter entry result only if going back in history");
//            if (enterEntry.path instanceof ReceivesNavigationResult) {
//                ((ReceivesNavigationResult) enterEntry.path).onReceiveNavigationResult(enterEntry.navigationResult);
//            }
//            enterEntry.navigationResult = null;
//        }


//        if (nextEntryResult != null) {
//            Preconditions.checkArgument(!inHistory, "Next entry result only if entry is dead");
//            if (enterEntry.path instanceof ReceivesNavigationResult) {
//                ((ReceivesNavigationResult) enterEntry.path).onReceiveNavigationResult(nextEntryResult);
//            }
//            nextEntryResult = null;
//        }

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

        final int direction = dispatch.direction != 0 ? dispatch.direction : (forward ? ViewTransition.DIRECTION_FORWARD : ViewTransition.DIRECTION_BACKWARD);

        present(enterEntry, exitEntry, forward, direction);
    }

    private void present(final History.Entry enterEntry, final History.Entry exitEntry, final boolean forward, final int transitionDirection) {
//        MortarScope currentScope = navigator.presenter.getCurrentScope();
//        Preconditions.checkNotNull(currentScope, "Current scope cannot be null");
//        Logger.d("Current container scope is: %s", currentScope.getName());

        final MortarScope exitScope = navigator.scoper.getCurrentScope(exitEntry.path);
        ScopedEntry scopedEntry = createDispatchEntry(enterEntry, forward);

        navigator.presenter.present(scopedEntry, exitEntry, forward, transitionDirection, new Callback() {
            @Override
            public void onComplete() {
                Logger.d("Destroy scope: %s", exitScope.getName());
                exitScope.destroy();

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

    private ScopedEntry createDispatchEntry(History.Entry entry, boolean forward) {
        return new ScopedEntry(entry, navigator.scoper.getNewScope(entry.path, forward));
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

    static class Dispatch {
        final History.Entry entry;
        final Object result;
        final int direction;

        public Dispatch(History.Entry entry, Object result, int direction) {
            this.entry = entry;
            this.result = result;
            this.direction = direction;
        }
    }

    static class ScopedEntry {
        final History.Entry entry;
        final MortarScope scope;

        public ScopedEntry(History.Entry entry, MortarScope scope) {
            Preconditions.checkNotNull(entry, "Entry null");
            Preconditions.checkNotNull(scope, "Scope null");
            this.entry = entry;
            this.scope = scope;
        }
    }
}
