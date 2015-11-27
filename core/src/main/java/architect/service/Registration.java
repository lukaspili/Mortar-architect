package architect.service;

import architect.Executor;
import architect.Hooks;

public interface Registration<T extends Controller, E extends Presenter> {

    T createController(Executor executor);

    E createPresenter(Hooks hooks);

    Delegate createDelegate();
}