package architect.service.commons;

import architect.Callback;

/**
 * Stateful callback
 *
 * Created by lukasz on 23/11/15.
 */
public abstract class PresentationCallback implements Callback {

    protected final int presentationSessionId;

    public PresentationCallback(int presentationSessionId) {
        this.presentationSessionId = presentationSessionId;
    }

    public abstract void onComplete();
}
