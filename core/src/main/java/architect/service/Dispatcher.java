package architect.service;

import architect.Callback;
import architect.DispatchEnv;
import architect.History;

public interface Dispatcher {

    void dispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward, DispatchEnv env, Callback callback);

}