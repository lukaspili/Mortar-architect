package architect.service;

import architect.Executor;

public interface Registration<T extends Controller, E extends Dispatcher> {

    T createController(Executor executor);

    E createDispatcher();
}