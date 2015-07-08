package architect;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

import java.util.List;

import architect.transition.ViewTransition;
import architect.view.HasPresenter;
import mortar.MortarScope;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class Presenter {

    private final Transitions transitions;
    private NavigatorView view;
    private Dispatcher.Callback dispatchingCallback;
    private boolean active;

    /**
     * Track the session
     * Each session lives during the lifespan of a navigation view instance
     * When a new view is provided, new unique session id is set
     *
     * id starts at 1
     * negative value means there is no session (like during config changes)
     */
    private int sessionId = 0;
//    private final List<History.Entry> entriesInView = new ArrayList<>();
//    private final List<MortarScope> scopesInView = new ArrayList<>();
//    private final List<String> presentedModalScopes = new ArrayList<>();

    Presenter(Transitions transitions) {
        this.transitions = transitions;
    }

    void newSession() {
        Preconditions.checkArgument(sessionId <= 0, "New session while session id is valid");
        sessionId *= -1;
        sessionId++;
    }

    void invalidateSession() {
        Preconditions.checkArgument(sessionId > 0, "Invalidate session while session id is invalid");
        sessionId *= -1;
    }

    void attach(NavigatorView view) {
        Preconditions.checkNotNull(view, "Cannot attach null navigator view");
        Preconditions.checkNull(this.view, "Current navigator view not null, did you forget to detach the previous view?");
        Preconditions.checkArgument(!active, "Navigator view must be inactive before attaching");
        Preconditions.checkNull(dispatchingCallback, "Dispatching callback must be null before attaching");

        newSession();
        this.view = view;
        this.view.sessionId = sessionId;
    }

    void detach() {
        Preconditions.checkNotNull(view, "Cannot detach null navigator view");
        Preconditions.checkArgument(!active, "Navigator view must be inactive before detaching");
        Preconditions.checkNull(dispatchingCallback, "Dispatching callback must be null before detaching");

        invalidateSession();
        view = null;
    }

    void activate() {
        Preconditions.checkNotNull(view, "Navigator view cannot be null, did you forget to attach?");
        Preconditions.checkNull(dispatchingCallback, "Dispatching callback must be null before activating");
        active = true;

    }

    void desactivate() {
        Preconditions.checkNotNull(view, "Navigator view cannot be null, did you forget to attach?");
        active = false;

        completeDispatchingCallback();
    }

    void restore(List<Dispatcher.Dispatch> modals) {
        Preconditions.checkNotNull(view, "Container view cannot be null");
        Preconditions.checkArgument(view.getChildCount() == 0, "Restore requires view with no children");
        if (modals.isEmpty()) {
            return;
        }

        Dispatcher.Dispatch dispatch;
        View child;
        for (int i = 0; i < modals.size(); i++) {
            dispatch = modals.get(i);
            Logger.d("Restore modal: %s", dispatch.entry.scopeName);
            child = dispatch.entry.factory.createView(dispatch.scope.createContext(view.getContext()));
            if (dispatch.entry.state != null) {
                view.restoreHierarchyState(dispatch.entry.state);
            }

            view.addView(child);
        }
    }

    void present(final Dispatcher.Dispatch newDispatch, final History.Entry previousEntry, final Dispatcher.Direction direction, final Dispatcher.Callback callback) {
        Preconditions.checkNotNull(view, "Container view cannot be null");
        Preconditions.checkNull(dispatchingCallback, "Previous dispatching callback not completed");

        Logger.d("Present new dispatch: %s - with direction: %s", newDispatch.entry.scopeName, direction);
        Logger.d("Previous entry: %s", previousEntry.scopeName);

        // set and track the callback from dispatcher
        // dispatcher is waiting for the onComplete call
        // either when present is done, or when presenter is desactivated
        dispatchingCallback = callback;

        if (direction == Dispatcher.Direction.FORWARD && !previousEntry.dead) {
            // forward, save previous view state
            Logger.d("Save view state for: %s", previousEntry.scopeName);
            previousEntry.state = getCurrentViewState();
        }

        // create or reuse view
        View newView;
        boolean addNewView;
        if (direction == Dispatcher.Direction.FORWARD ||
                (direction == Dispatcher.Direction.BACKWARD && !previousEntry.isModal())) {
            // create new view when forward and replace
            // or when backward if previous entry is not modal
            Logger.d("Create new view for %s", newDispatch.entry.scopeName);
            Context context = newDispatch.scope.createContext(view.getContext());
            newView = newDispatch.entry.factory.createView(context);
            addNewView = true;
        } else {
            Logger.d("Reuse previous view for %s", newDispatch.entry.scopeName);
            newView = view.getChildAt(view.getChildCount() - 2);
            addNewView = false;
        }

        // find transition
        ViewTransition transition;
        if (view.hasCurrentView()) {
            transition = transitions.findTransition(view.getCurrentView(), newView, direction);
        } else {
            transition = null;
        }

        // restore state if it exists
        if (newDispatch.entry.state != null) {
            Logger.d("Restore view state for: %s", newDispatch.entry.scopeName);
            newView.restoreHierarchyState(newDispatch.entry.state);
        }

        if (newDispatch.entry.receivedResult != null) {
            if (newView instanceof HasPresenter) {
                // put result
                ViewPresenter viewPresenter = ((HasPresenter) newView).getPresenter();
                if (viewPresenter instanceof ReceivesResult) {
                    ((ReceivesResult) viewPresenter).onReceivedResult(newDispatch.entry.receivedResult);
                }
            }
            newDispatch.entry.receivedResult = null;
        }

        boolean keepPreviousView = direction == Dispatcher.Direction.FORWARD && newDispatch.entry.isModal();
        Logger.d("Keep previous view: %b", keepPreviousView);

        view.show(newView, addNewView, !keepPreviousView, direction, transition, new PresentationCallback() {
            @Override
            public void onPresentationFinished(int sessionId) {
                if (isCurrentSession(sessionId)) {
                    completeDispatchingCallback();
                }
            }
        });
    }

    MortarScope getCurrentScope() {
        return view.hasCurrentView() ? MortarScope.getScope(view.getCurrentView().getContext()) : null;
    }

    boolean containerViewOnBackPressed() {
        return view != null && view.onBackPressed();
    }

    private SparseArray<Parcelable> getCurrentViewState() {
        Preconditions.checkNotNull(view, "Container view cannot be null");
        Preconditions.checkArgument(view.hasCurrentView(), "Save view state requires current view");

        View view = this.view.getCurrentView();
        SparseArray<Parcelable> state = new SparseArray<>();
        view.saveHierarchyState(state);
        return state;
    }

    boolean isActive() {
        return view != null && active;
    }

    boolean isCurrentSession(int sessionId) {
        return this.sessionId == sessionId;
    }

    private void completeDispatchingCallback() {
        if (dispatchingCallback == null) {
            return;
        }

        Dispatcher.Callback callback = dispatchingCallback;
        dispatchingCallback = null;  // onComplete redispatches direclty so we need to remove ref now
        callback.onComplete();
    }

    interface PresentationCallback {

        void onPresentationFinished(int sessionId);
    }
}