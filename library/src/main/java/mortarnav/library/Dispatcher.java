package mortarnav.library;

import android.content.Context;
import android.view.View;

import mortar.MortarScope;
import mortarnav.library.context.ScreenContextFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Dispatcher implements NavigatorContainerManager.Listener {

    private final History history;
    private final NavigatorContainerManager containerManager;
    private final ScreenContextFactory screenContextFactory = new ScreenContextFactory();
    private boolean dispatching;

    public Dispatcher(History history, NavigatorContainerManager containerManager) {
        this.history = history;
        this.containerManager = containerManager;
        containerManager.addListener(this);
    }

//    public Dispatcher(NavigatorContainerManager containerManager) {
////        this.pendingNavigations = new ArrayDeque<>();
//        this.containerManager = containerManager;
//        containerManager.addListener(this);
//    }

//    public void configureContainerView(NavigatorContainerView containerView) {
//        Preconditions.checkNull(this.containerView, "Container view not null");
//        Preconditions.checkNotNull(containerView, "Param container view null");
//
//        this.containerView = containerView;
//        containerView.setDispatcher(this);
//
//        if (pendingNavigation != null) {
//            dispatch();
//        }
//    }
//
//    public void removeContainerView() {
//        Preconditions.checkNotNull(containerView, "Container view null");
//        containerView = null;
//
//        if (pendingNavigation != null) {
//            // navigation in process
//            // go directly to end state
//            //TODO
//        }
//    }

//    public void enqueue() {
//        PendingNavigation pendingNav = new PendingNavigation();
//        pendingNavigations.add(pendingNav);
//
//        if (pendingNavigation == null) {
//            // idle, dispatch now
//            pendingNavigation = pendingNav;
//            dispatch();
//        }
//
//
////        else {
////            // enqueue for next
////            // if there was another next already set, replace it
////            nextPendingNavigation = pendingNav;
////        }
//    }

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

        History.Entry last = history.peek();
        Preconditions.checkNotNull(last, "Cannot dispatch empty history");

        System.out.println("last screen history is " + last.getScreen());

        Context currentContext = containerManager.getCurrentViewContext();
        if (currentContext != null) {
            MortarScope currentScope = MortarScope.getScope(currentContext);
            if (currentScope != null) {
                if (currentScope.getName().equals(last.getScreen().getScopeName())) {
                    // history in sync with current element, work is done
                    System.out.println("history in sync with current element, dispatch stop");
                    return;
                }

                History.Entry existingEntry = history.findScreen(currentScope.getName());
                if (existingEntry == null) {
                    // a scope already exists for the current view, but not anymore in history
                    // destroy it
                    System.out.println("destroy scope " + currentScope.getName());
                    currentScope.destroy();
                } else {
                    // scope still exists in history, save view state
                    System.out.println("save current view state");
                    existingEntry.setState(containerManager.getCurrentViewState());
                }
            }
        }

        Context context = screenContextFactory.setUp(containerManager.getContainerContext(), last.getScreen());

        View view = last.getScreen().createView(context);
        if (last.getState() != null) {
            System.out.println("restore state for " + view);
            view.restoreHierarchyState(last.getState());
        }

        System.out.println("show view " + view);
        containerManager.show(view, new NavigatorContainerManager.Callback() {
            @Override
            public void onShowEnd() {
                dispatching = false;
                dispatch();
            }
        });
    }


    // NavigatorContainerManager.Listener

    @Override
    public void onContainerReady() {
        System.out.println("onContainerReady");
    }
}
