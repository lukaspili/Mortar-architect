package architect.service;

import architect.Executor;

public interface Registration<T extends Controller, E extends Presenter> {

    T createController(Executor executor);

    E createPresenter();

    Delegate createDelegate();
}