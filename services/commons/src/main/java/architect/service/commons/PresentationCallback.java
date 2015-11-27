package architect.service.commons;

import architect.Callback;

/**
 * Stateful callback
 *
 * Created by lukasz on 23/11/15.
 */
public abstract class PresentationCallback implements Callback {

    protected final int callbackSessionId;

    public PresentationCallback(int callbackSessionId) {
        this.callbackSessionId = callbackSessionId;
    }

    public abstract void onComplete();
}
