package architect.service;

import architect.Executor;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class Controller {

    protected final Executor executor;

    public Controller(Executor executor) {
        this.executor = executor;
    }

    public final boolean pop() {
        return executor.popAnyService();
    }
}
