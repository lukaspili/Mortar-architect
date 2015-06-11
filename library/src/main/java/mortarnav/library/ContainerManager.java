package mortarnav.library;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class ContainerManager {

    private final Dispatcher dispatcher;
    private final TransitionManager containerTransitionner;
    private NavigatorContainerView containerView;

    ContainerManager(Dispatcher dispatcher, ContainerTransitions transitions) {
        this.dispatcher = dispatcher;
        containerTransitionner = new TransitionManager(transitions);
    }

    void setContainerView(NavigatorContainerView view) {
        Preconditions.checkNotNull(view, "New containerView null");
        Preconditions.checkNull(containerView, "Current containerView not null");
        containerView = view;
        containerView.setTransitionManager(containerTransitionner);

        dispatcher.onContainerReady();
    }

    void removeContainerView() {
        Preconditions.checkNotNull(containerView, "Current containerView null");
        containerView = null;
    }

    boolean containerViewOnBackPressed() {
        return containerView != null && containerView.onBackPressed();
    }

    Context getCurrentViewContext() {
        if (!containerView.hasCurrentView()) {
            return null;
        }

        return containerView.getCurrentView().getContext();
    }

    SparseArray<Parcelable> getCurrentViewState() {
        checkPreconditions();
        Preconditions.checkArgument(containerView.hasCurrentView(), "Save view state requires current view");

        View view = containerView.getCurrentView();
        SparseArray<Parcelable> state = new SparseArray<>();
        view.saveHierarchyState(state);
        return state;
    }

    void performTransition(final View view, final Dispatcher.Direction direction, final Dispatcher.TraversalCallback callback) {
        checkPreconditions();
        containerView.performTransition(view, direction, callback);
    }

    private void checkPreconditions() {
        Preconditions.checkNotNull(containerView, "Container view cannot be null");
    }

    boolean isReady() {
        return containerView != null;
    }

    Context getContainerContext() {
        Preconditions.checkNotNull(containerView, "Container view for its context cannot be null");
        return containerView.getContext();
    }

    public interface Listener {
        void onContainerReady();
    }
}