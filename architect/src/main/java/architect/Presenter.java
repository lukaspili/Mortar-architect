package architect;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class Presenter {

    private final TransitionExecutor transitionExecutor;
    private NavigatorView view;
    private PresenterSession session; // 1 session <=> 1 view
    private Dispatcher.Callback dispatchingCallback;
    private boolean active;

    Presenter(Transitions transitions) {
        transitionExecutor = new TransitionExecutor(transitions);
    }

    void attach(NavigatorView view) {
        Preconditions.checkNotNull(view, "Cannot attach null navigator view");
        Preconditions.checkNull(this.view, "Current navigator view not null, did you forget to detach the previous view?");
        Preconditions.checkNull(session, "Session must be null before attaching");
        Preconditions.checkArgument(!active, "Navigator view must be inactive before attaching");
        Preconditions.checkNull(dispatchingCallback, "Dispatching callback must be null before attaching");

        this.view = view;
        session = new PresenterSession();
    }

    void detach() {
        Preconditions.checkNotNull(view, "Cannot detach null navigator view");
        Preconditions.checkNotNull(session, "Session must be not null before detaching");
        Preconditions.checkArgument(!active, "Navigator view must be inactive before detaching");
        Preconditions.checkNull(dispatchingCallback, "Dispatching callback must be null before detaching");

        view = null;
        session = null;
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

    void present(History.Entry entry, MortarScope newScope, final Dispatcher.Direction direction, final Dispatcher.Callback callback) {
        Preconditions.checkNotNull(view, "Container view cannot be null");
        Preconditions.checkNull(dispatchingCallback, "Previous dispatching callback not completed");

        Logger.d("Present new scope: %s - with direction: %s", newScope.getName(), direction);

        // set and track the callback from dispatcher
        // dispatcher is waiting for the onComplete call
        // either when present is done, or when presenter is desactivated
        dispatchingCallback = callback;

        Context context = newScope.createContext(view.getContext());
        View newView = entry.factory.createView(context);

        if (entry.state != null) {
            newView.restoreHierarchyState(entry.state);
        }

        view.startPresentation(newView, direction, session, new PresentationCallback() {
            @Override
            public void onPresentationFinished(View exitView, View enterView, PresenterSession session) {
                if (!isSessionValid(session)) return;

                if (exitView == null) {
                    // no previous view, don't animate
                    view.endPresentation();
                    completeDispatchingCallback();
                    return;
                }

                transitionExecutor.makeTransition(exitView, enterView, direction, session, new TransitionCallback() {
                    @Override
                    public void onTransitionFinished(PresenterSession session) {
                        if (!isSessionValid(session)) return;
                        view.endPresentation();
                        completeDispatchingCallback();
                    }
                });
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

    private boolean isSessionValid(PresenterSession session) {
        return isActive() && this.session == session;
    }

    private void completeDispatchingCallback() {
        if (dispatchingCallback == null) {
            return;
        }

        dispatchingCallback.onComplete();
        dispatchingCallback = null;
    }

    /**
     * Session is the same for all events done between onAttach() and onDetach()
     * Once onAttach() is called again, new session is created
     */
    class PresenterSession {

    }

    interface PresentationCallback {

        void onPresentationFinished(View exitView, View enterView, PresenterSession session);
    }

    interface TransitionCallback {

        void onTransitionFinished(PresenterSession session);
    }
}