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
    private final List<Listener> listeners;

    public NavigatorContainerManager() {
        listeners = new ArrayList<>();
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void setContainerView(NavigatorContainerView view) {
        Preconditions.checkNotNull(view, "New containerView null");
        Preconditions.checkNull(containerView, "Current containerView not null");
        containerView = view;
        for (Listener listener : listeners) {
            listener.onContainerReady();
        }
    }

    public void removeContainerView() {
        Preconditions.checkNotNull(containerView, "Current containerView null");
        containerView = null;
    }

    public Context getCurrentViewContext() {
        if (!containerView.hasCurrentView()) {
            return null;
        }

        return containerView.getCurrentView().getContext();
    }

    public SparseArray<Parcelable> getCurrentViewState() {
        checkPreconditions();
        Preconditions.checkArgument(containerView.hasCurrentView(), "Save view state requires current view");

        View view = containerView.getCurrentView();
        SparseArray<Parcelable> state = new SparseArray<>();
        view.saveHierarchyState(state);
        return state;
    }

    public void show(View view, final Callback callback) {
        checkPreconditions();

        containerView.transitionView(view, new NavigatorContainerView.Callback() {
            @Override
            public void onTransitionEnd() {
                callback.onShowEnd();
            }
        });
    }

    private void checkPreconditions() {
        Preconditions.checkNotNull(containerView, "Container view null");
    }

    public boolean isReady() {
        return containerView != null;
    }

    public Context getContainerContext() {
        Preconditions.checkNotNull(containerView, "containerView null, cannot provide its context");
        return containerView.getContext();
    }

    public interface Listener {
        void onContainerReady();
    }

    public interface Callback {
        void onShowEnd();
    }
}