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
    //    private final TransitionExecutor transitionExecutor;
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

    //    private final SparseArray<String> scopesInView = new SparseArray<>();
    private final List<String> scopesInView = new ArrayList<>();

    Presenter(Transitions transitions) {
        this.transitions = transitions;
//        transitionExecutor = new TransitionExecutor();
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

    boolean isCurrentSession(int sessionId) {
        return this.sessionId == sessionId;
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

    void present(final History.Entry entry, MortarScope newScope, final Dispatcher.Direction direction, final Dispatcher.Callback callback) {
        Preconditions.checkNotNull(view, "Container view cannot be null");
        Preconditions.checkNull(dispatchingCallback, "Previous dispatching callback not completed");

        Logger.d("Present new scope: %s - with direction: %s", newScope.getName(), direction);

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
            if (!scopesInView.isEmpty() && scopesInView.contains(entry.scopeName)) {
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
            Logger.d("Find transition %s - %s - %s", newView, view.getCurrentView(), direction);
            transition = transitions.findTransition(newView, view.getCurrentView(), direction);
        } else {
            transition = null;
        }

        // 4. remove previous scope
        if (!scopesInView.isEmpty()) {
            if (direction != Dispatcher.Direction.FORWARD ||
                    (transition == null || transition.removeExitView())) {
                String scope = scopesInView.remove(scopesInView.size() - 1);
                Logger.d("Remove scope in view: %s", scope);
            }
        }

        // 5. add new scope in view
        scopesInView.add(entry.scopeName);

        Logger.d("### debug scopes in view");
        int i = 0;
        for (String string : scopesInView) {
            Logger.d("%d - %s", i++, string);
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


//        view.startPresentation(newView, direction, sessionId, new PresentationCallback() {
//            @Override
//            public void onPresentationFinished(View exitView, View enterView, PresenterSession session) {
//                if (!isSessionValid(session)) return;
//
//                if (exitView == null) {
//                    // no previous view, don't animate
//                    view.endPresentation(true);
//                    completeDispatchingCallback();
//                    return;
//                }
//
//                transitionExecutor.makeTransition(exitView, enterView, direction, session, new TransitionCallback() {
//                    @Override
//                    public void onTransitionFinished(PresenterSession session, boolean removePreviousView) {
//                        if (!isSessionValid(session)) return;
//
//
////                        if (!removePreviousView) {
////                            int indexPreviousView = view.getChildCount() - 2;
////                            view.getTag()
////                        }
//
//                        view.endPresentation(removePreviousView); // remove previous view if transition is ScreenTransition
//                        completeDispatchingCallback();
//                    }
//                });
//            }
//        });
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

//    private boolean isSessionValid(PresenterSession session) {
//        return isActive() && this.sessionId == session;
//    }

    private void completeDispatchingCallback() {
        if (dispatchingCallback == null) {
            return;
        }

        Dispatcher.Callback callback = dispatchingCallback;
        dispatchingCallback = null;  // onComplete redispatches direclty so we need to remove ref now
        callback.onComplete();
    }

//    /**
//     * Session is the same for all events done between onAttach() and onDetach()
//     * Once onAttach() is called again, new session is created
//     */
//    class PresenterSession {
//
//    }

    interface PresentationCallback {

        void onPresentationFinished(int sessionId);
    }

//    interface TransitionCallback {
//
//        void onTransitionFinished(PresenterSession session, boolean removePreviousView);
//    }
}