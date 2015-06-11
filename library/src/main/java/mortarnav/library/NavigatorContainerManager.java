package mortarnav.library;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorContainerManager {

    private NavigatorContainerView containerView;
    private final NavigatorContainerTransitionner containerTransitionner;
    private final List<Listener> listeners;

    NavigatorContainerManager(NavigatorTransitions transitions) {
        containerTransitionner = new NavigatorContainerTransitionner(transitions);
        listeners = new ArrayList<>();
    }

    void addListener(Listener listener) {
        listeners.add(listener);
    }

    void setContainerView(NavigatorContainerView view) {
        Preconditions.checkNotNull(view, "New containerView null");
        Preconditions.checkNull(containerView, "Current containerView not null");
        containerView = view;
        containerView.setNavigatorContainerTransitionner(containerTransitionner);
        for (Listener listener : listeners) {
            listener.onContainerReady();
        }
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