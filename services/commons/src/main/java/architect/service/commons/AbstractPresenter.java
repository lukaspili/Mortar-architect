package architect.service.commons;

import android.content.Context;
import android.view.View;

import architect.Callback;
import architect.History;
import architect.Hooks;
import architect.Processing;
import architect.service.Presenter;

/**
 * Created by lukasz on 23/11/15.
 */
public abstract class AbstractPresenter<T extends Container, E> extends Presenter<T> implements HandlesBack {

    protected final Hooks hooks;
    protected final Transitions<E> transitions;

    public AbstractPresenter(Hooks hooks, Transitions<E> transitions) {
        this.hooks = hooks;
        this.transitions = transitions;
    }

    /**
     * Track the session
     * Each session lives during the lifespan of a navigation view instance
     * When a new view is provided, new unique session id is set
     *
     * id starts at 1
     * negative value means there is no session (like during config changes)
     */
    private int sessionId;
    private PresentationCallback currentPresentationCallback;

    @Override
    public void takeContainer(T container) {
        super.takeContainer(container);
        newSession();
    }

    @Override
    public void dropContainer(T container) {
        super.dropContainer(container);

        completePresentationCallback();
        invalidateSession();
    }

    private void newSession() {
        Preconditions.checkArgument(sessionId <= 0, "New session while session id is valid");
        sessionId *= -1;
        sessionId++;
    }

    private void invalidateSession() {
        Preconditions.checkArgument(sessionId > 0, "Invalidate session while session id is invalid");
        sessionId *= -1;
    }

    protected void initPresentationCallback(final Callback callback) {
        currentPresentationCallback = new PresentationCallback(sessionId) {
            @Override
            public void onComplete() {
                if (!isSessionValid(callbackSessionId)) {
                    return;
                }

                currentPresentationCallback = null;
                callback.onComplete();
            }
        };
    }

    protected void completePresentationCallback() {
        if (currentPresentationCallback != null) {
            currentPresentationCallback.onComplete();
        }
    }

    protected boolean isSessionValid(int sessionId) {
        return this.sessionId == sessionId;
    }

    protected Context getContext(View containerView, History.Entry entry, Processing processing) {
        Context context = hooks.getPresenterHooks().getOverridedContext(containerView, entry, processing);
        if (context != null) {
            return context;
        }

        return containerView.getContext();
    }
}
