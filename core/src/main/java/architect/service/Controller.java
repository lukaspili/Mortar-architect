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

    public boolean pop() {
        return executor.pop();
    }

    public boolean pop(Object result) {
        return executor.pop(result);
    }
}
