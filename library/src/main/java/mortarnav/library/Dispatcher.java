package mortarnav.library;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Dispatcher {

    private final NavigationCallback callback;

    private NavigatorContainerView containerView;
    private PendingNavigation pendingNavigation;
    private PendingNavigation nextPendingNavigation;

    public Dispatcher(NavigationCallback callback) {
        Preconditions.checkNotNull(callback, "Callback null");
        this.callback = callback;
    }

    public void configureContainerView(NavigatorContainerView containerView) {
        Preconditions.checkNull(this.containerView, "Container view not null");
        Preconditions.checkNotNull(containerView, "Param container view null");

        this.containerView = containerView;
        containerView.setDispatcher(this);

        if (pendingNavigation != null) {
            dispatch();
        }
    }

    public void removeContainerView() {
        Preconditions.checkNotNull(containerView, "Container view null");
        containerView = null;

        if (pendingNavigation != null) {
            // navigation in process
            // go directly to end state
            //TODO
        }
    }

    public void enqueue(Screen screen) {
        Preconditions.checkNotNull(screen, "Screen null");

        PendingNavigation pendingNav = new PendingNavigation(screen);

        if (pendingNavigation == null) {
            // idle, dispatch now
            pendingNavigation = pendingNav;
            dispatch();
        } else {
            // enqueue for next
            // if there was another next already set, replace it
            nextPendingNavigation = pendingNav;
        }
    }

    private void dispatch() {
        Preconditions.checkNotNull(pendingNavigation, "pendingNavigation null");

        if (containerView == null) {
            // wait for container view to be set
            return;
        }

        callback.onDispatched(pendingNavigation);
        containerView.makeTransition(pendingNavigation);
    }

    public void onTransitionEnd() {
        Preconditions.checkNotNull(pendingNavigation, "pendingNavigation null");

        pendingNavigation = nextPendingNavigation;
        nextPendingNavigation = null;

        if (pendingNavigation != null) {
            dispatch();
        }
    }

    public static class PendingNavigation {

        private final Screen destination;

        public PendingNavigation(Screen destination) {
            this.destination = destination;
        }

        //        private final Direction direction;

//        public PendingNavigation(Screen destination, Direction direction) {
//            this.destination = destination;
//            this.direction = direction;
//        }

        public Screen getDestination() {
            return destination;
        }

//        public Direction getDirection() {
//            return direction;
//        }

        public enum Direction {
            BACKWARD, FORWARD, REPLACE
        }
    }

    public interface NavigationCallback {

        void onDispatched(PendingNavigation pendingNavigation);
    }
}
