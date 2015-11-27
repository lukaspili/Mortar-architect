package architect.service;

import java.util.List;

import architect.Callback;
import architect.History;
import architect.Processing;

public abstract class Presenter<T> {

    protected T container;

    public void takeContainer(T container) {
        this.container = container;
    }

    public void dropContainer(T container) {
        this.container = null;
    }

    public abstract void present(History.Entry enterEntry, History.Entry exitEntry, boolean forward, Processing env, Callback callback);

    public abstract void restore(List<History.Entry> entries, Processing processing);



}