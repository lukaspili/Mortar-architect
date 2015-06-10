package mortarnav.library;

import android.content.Context;
import android.view.View;

import mortar.MortarScope;
import mortarnav.library.screen.ScreenContextFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Dispatcher implements NavigatorContainerManager.Listener {

    private final History history;
    private final NavigatorContainerManager containerManager;
    private final ScreenContextFactory contextFactory;
    private boolean dispatching;

    public Dispatcher(History history, NavigatorContainerManager containerManager, ScreenContextFactory contextFactory) {
        this.history = history;
        this.containerManager = containerManager;
        this.contextFactory = contextFactory;

        containerManager.addListener(this);
    }

    public void dispatch() {
        System.out.println("DISPATCH!");

        if (!containerManager.isReady()) {
            System.out.println("container view not set, dispatcher is waiting");
            // wait for container view to be set
            return;
        }

        if (dispatching) {
            System.out.println("already dispatching, stop and wait");
            return;
        }
        dispatching = true;

        History.Entry last = history.peek();
        Preconditions.checkNotNull(last, "Cannot dispatch empty history");

        System.out.println("last screen history is " + last.getScreen());

        Direction direction = Direction.FORWARD;

        Context currentContext = containerManager.getCurrentViewContext();
        if (currentContext != null) {
            MortarScope currentScope = MortarScope.getScope(currentContext);
            if (currentScope != null) {
                if (currentScope.getName().equals(last.getScreen().getScopeName())) {
                    // history in sync with current element, work is done
                    System.out.println("history in sync with current element, dispatch stop");
                    endDispatch();
                    return;
                }

                History.Entry existingEntry = history.findScreen(currentScope.getName());
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
                    existingEntry.setState(containerManager.getCurrentViewState());
                    direction = Direction.FORWARD;
                }
            }
        }

        Context context = contextFactory.setUp(containerManager.getContainerContext(), last.getScreen());

        View view = last.getScreen().createView(context);
        if (last.getState() != null) {
            // restore state for the new view, if exists
            System.out.println("restore state for " + view);
            view.restoreHierarchyState(last.getState());
        }

        System.out.println("show view " + view);
        containerManager.performTransition(view, direction, new TraversalCallback() {
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


    // NavigatorContainerManager.Listener

    @Override
    public void onContainerReady() {
        System.out.println("onContainerReady");
    }

    public enum Direction {
        FORWARD, BACKWARD
    }

    public interface TraversalCallback {
        void onTraversalCompleted();
    }
}
