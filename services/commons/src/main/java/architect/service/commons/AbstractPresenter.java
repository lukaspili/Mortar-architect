package architect.service.commons;

import architect.service.Presenter;

/**
 * Created by lukasz on 23/11/15.
 */
public abstract class AbstractPresenter<T extends Container> extends Presenter<T> implements HandlesBack {

    /**
     * Track the session
     * Each session lives during the lifespan of a navigation view instance
     * When a new view is provided, new unique session id is set
     *
     * id starts at 1
     * negative value means there is no session (like during config changes)
     */
    protected int sessionId;

    @Override
    public void takeContainer(T container) {
        super.takeContainer(container);
        newSession();
    }

    @Override
    public void dropContainer(T container) {
        super.dropContainer(container);
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

    protected boolean isSessionValid(int sessionId) {
        return this.sessionId == sessionId;
    }
}
