package architect;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
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
    private final List<History.Entry> entriesInView = new ArrayList<>();
    private final List<MortarScope> scopesInView = new ArrayList<>();

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

    void present(final History.Entry entry, MortarScope newScope, final Dispatcher.Direction direction, final Dispatcher.Callback callback) {
        Preconditions.checkNotNull(view, "Container view cannot be null");
        Preconditions.checkNull(dispatchingCallback, "Previous dispatching callback not completed");

        Logger.d("Present new scope: %s - with direction: %s", newScope.getName(), direction);

        if (firstDispatch) {
            firstDispatch = false;
            Logger.d("First dispatch");
            Logger.d("Entries in view: %d", entriesInView.size());

            if (!entriesInView.isEmpty()) {
                Logger.d("### BEFORE debug scopes in view");
                int ii = 0;
                for (MortarScope s : scopesInView) {
                    Logger.d("%d - %s", ii++, s);
                }

                Preconditions.checkArgument(scopesInView.get(scopesInView.size() - 1) == newScope, "The last scope in view does not match with the first scope to show");

                if (entriesInView.size() > 1) {
                    // restore all scopes in view
                    Context existingContext;
                    View existingView;
                    for (int i = 0; i < entriesInView.size() - 1; ++i) {
                        existingContext = scopesInView.get(i).createContext(view.getContext());
                        existingView = entry.factory.createView(existingContext);
                        view.addView(existingView);
                        Logger.d("Add view %s", existingView);
                    }
                }
            }
        }

        // set and track the callback from dispatcher
        // dispatcher is waiting for the onComplete call
        // either when present is done, or when presenter is desactivated
        dispatchingCallback = callback;

        // 1. define if we have to create the view of the new scope
        boolean createView;
        if (direction == Dispatcher.Direction.FORWARD || direction == Dispatcher.Direction.REPLACE) {
            // always create when forward and replace
            createView = true;
        } else {
            // reuse view if it exists
            if (!entriesInView.isEmpty() && entriesInView.contains(entry.scopeName)) {
                Logger.d("Scope in view exists: %s", entry.scopeName);
                createView = false;
            } else {
                createView = true;
            }
        }

        // 2. create or reuse view
        View newView;
        int newViewIndex;
        if (createView) {
            Logger.d("Create new view for %s", entry.scopeName);
            Context context = newScope.createContext(view.getContext());
            newView = entry.factory.createView(context);
            newViewIndex = -1;
        } else {
            Logger.d("Reuse previous view for %s", entry.scopeName);
            newView = view.getChildAt(view.getChildCount() - 2);
            newViewIndex = view.getChildCount() - 2;
        }

        // 3. find transition
        ViewTransition transition;
        if (view.hasCurrentView()) {
            transition = transitions.findTransition(view.getCurrentView(), newView, direction);
        } else {
            transition = null;
        }

        // remove previous scope if backward / replace with not empty scope (empty means first init) / forward with transition remove exitview
        if (direction == Dispatcher.Direction.BACKWARD ||
                (direction == Dispatcher.Direction.REPLACE && !entriesInView.isEmpty()) ||
                (direction == Dispatcher.Direction.FORWARD && transition != null && transition.removeExitView())) {
            entriesInView.remove(entriesInView.size() - 1);
            MortarScope removed = scopesInView.remove(scopesInView.size() - 1);
            Logger.d("Remove scope in view: %s", removed.getName());
        }

        if (createView) {
            entriesInView.add(entry);
            scopesInView.add(newScope);
            Logger.d("Add scope in view: %s", newScope.getName());
        }

        Logger.d("### debug scopes in view");
        int i = 0;
        for (MortarScope s : scopesInView) {
            Logger.d("%d - %s", i++, s);
        }

        // 6. restore state if it exists
        if (entry.state != null) {
            newView.restoreHierarchyState(entry.state);
        }

        view.show(newView, newViewIndex, direction, transition, new PresentationCallback() {
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