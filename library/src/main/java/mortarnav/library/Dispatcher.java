package mortarnav.library;

import android.content.Context;
import android.view.View;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class Dispatcher {

    private final Navigator navigator;
    private boolean dispatching;
    private boolean stop;

    Dispatcher(Navigator navigator) {
        this.navigator = navigator;
    }

    public void stop() {
        stop = true;
    }

    public void dispatch() {
        if (stop || dispatching || !navigator.containerManager.isReady()) return;
        dispatching = true;
        Preconditions.checkNotNull(navigator.getScope(), "Dispatcher navigator scope cannot be null");
        Preconditions.checkArgument(!navigator.history.isEmpty(), "Cannot dispatch empty history");

        History.Entry last = navigator.history.peek();
        System.out.println("last screen history is " + last.getScreen());

        // default direction is FORWARD, unless we find previous scope in history
        Direction direction = Direction.FORWARD;

        Context currentContext = navigator.containerManager.getCurrentViewContext();
        if (currentContext != null) {
            MortarScope currentScope = MortarScope.getScope(currentContext);
            if (currentScope != null) {
                if (currentScope.getName().equals(last.getScreen().getMortarScopeName())) {
                    // history in sync with current element, work is done
                    System.out.println("history in sync with current element, dispatch stop");
                    endDispatch();
                    return;
                }

                History.Entry existingEntry = navigator.history.findScreen(currentScope.getName());
                if (existingEntry == null) {
                    // a scope already exists for the current view, but not anymore in history
                    // destroy it
                    // => backward navigation
                    System.out.println("Backward -> destroy scope " + currentScope.getName());
                    currentScope.destroy();
                    direction = Direction.BACKWARD;
                } else {
                    // scope still exists in history, save view state
                    // => forward navigation
                    System.out.println("Forward -> save current view state");
                    existingEntry.setState(navigator.containerManager.getCurrentViewState());
                    direction = Direction.FORWARD;
                }
            }
        }

        Context context = navigator.contextFactory.setUp(navigator.getScope(), navigator.containerManager.getContainerContext(), last.getScreen());

        View view = last.getScreen().createView(context);
        if (last.getState() != null) {
            // restore state for the new view, if exists
            System.out.println("restore state for " + view);
            view.restoreHierarchyState(last.getState());
        }

        System.out.println("show view " + view);
        navigator.containerManager.performTransition(view, direction, new TraversalCallback() {
            @Override
            public void onTraversalCompleted() {
                endDispatch();
                dispatch();
            }
        });
    }

    private void endDispatch() {
        Preconditions.checkArgument(dispatching, "Calling endDispatch while not dispatching");
        dispatching = false;
    }

    /**
     * Notified by the container manager when its ready
     */
    void onContainerReady() {
        System.out.println("onContainerReady");
        dispatch();
    }

    enum Direction {
        FORWARD, BACKWARD
    }

    public interface TraversalCallback {
        void onTraversalCompleted();
    }
}
