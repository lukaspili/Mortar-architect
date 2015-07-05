package architect;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

import java.util.List;

import architect.transition.ViewTransition;
import mortar.MortarScope;

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
    private boolean firstDispatch;
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
        Preconditions.checkArgument(!firstDispatch, "First dispatch true");

        newSession();
        this.view = view;
        this.view.sessionId = sessionId;
        firstDispatch = true;
    }

    void detach() {
        Preconditions.checkNotNull(view, "Cannot detach null navigator view");
        Preconditions.checkArgument(!active, "Navigator view must be inactive before detaching");
        Preconditions.checkNull(dispatchingCallback, "Dispatching callback must be null before detaching");

        invalidateSession();
        view = null;
        firstDispatch = false;
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
        Logger.d("Restore modals");
        for (Dispatcher.Dispatch modal : modals) {
            Logger.d("Modal: %s", modal.entry.scopeName);
        }

        if (modals.isEmpty()) {
            return;
        }

//        for (int i = 0; i < modals.size(); i++) {
//            Dispatcher.Dispatch m = modals.get(i);
//            Logger.d("For modal %s", m.entry.scopeName);
//            int index = modalScopes.indexOf(m.entry.scopeName);
//            if (index == -1) {
//                views.add(m.entry.factory.createView(m.scope.createContext(view.getContext())));
//                Logger.d("Create restore view");
//            } else {
//                Preconditions.checkArgument(index == i, "Modal scope index does not match");
//                indexes.add(index);
//                Logger.d("Keep existing modal scope");
//            }
//        }
    }

    void present(final Dispatcher.Dispatch newDispatch, final History.Entry previousEntry, final Dispatcher.Direction direction, final Dispatcher.Callback callback) {
        Preconditions.checkNotNull(view, "Container view cannot be null");
        Preconditions.checkNull(dispatchingCallback, "Previous dispatching callback not completed");

        if (direction == Dispatcher.Direction.BACKWARD) {
            Preconditions.checkNotNull(previousEntry, "Previous entry null cannot be null in backward presentation");
        }

        Logger.d("Present new dispatch: %s - with direction: %s", newDispatch.entry.scopeName, direction);
        Logger.d("Previous entry: %s", previousEntry != null ? previousEntry.scopeName : "NULL");

        // set and track the callback from dispatcher
        // dispatcher is waiting for the onComplete call
        // either when present is done, or when presenter is desactivated
        dispatchingCallback = callback;

        // create or reuse view
        View newView;
        boolean addNewView;
        if ((direction == Dispatcher.Direction.FORWARD || direction == Dispatcher.Direction.REPLACE) ||
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
            newView.restoreHierarchyState(newDispatch.entry.state);
        }

        boolean keepPreviousView = direction == Dispatcher.Direction.FORWARD && newDispatch.entry.isModal();
        Logger.d("Keep previous view: %b", keepPreviousView);

        view.show(newView, addNewView, !keepPreviousView, direction, transition, new PresentationCallback() {
            @Override
            public void onPresentationFinished(int sessionId) {
                completeDispatchingCallback();
            }
        });
    }

    MortarScope getCurrentScope() {
        return view.hasCurrentView() ? MortarScope.getScope(view.getCurrentView().getContext()) : null;
    }

    boolean containerViewOnBackPressed() {
        return view != null && view.onBackPressed();
    }

    SparseArray<Parcelable> getCurrentViewState() {
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