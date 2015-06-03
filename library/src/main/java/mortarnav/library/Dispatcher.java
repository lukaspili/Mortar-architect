package mortarnav.library;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Dispatcher {

    private PendingNavigation pendingNavigation;
    private PendingNavigation nextPendingNavigation;

    public void enqueue(Screen screen) {
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

    }

    public static class PendingNavigation {

        private Screen nextScreen;

        public PendingNavigation(Screen nextScreen) {
            this.nextScreen = nextScreen;
        }
    }
}
