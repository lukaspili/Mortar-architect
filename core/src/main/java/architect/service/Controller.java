package architect.service;

import architect.Controls;
import architect.Executor;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class Controller implements Controls {

    protected final Executor executor;

    public Controller(Executor executor) {
        this.executor = executor;
    }

    @Override
    public boolean pop() {
        return executor.pop();
    }

    @Override
    public boolean pop(Object result) {
        return executor.pop(result);
    }
}
