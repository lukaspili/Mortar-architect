package architect;

import android.os.Bundle;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Executor implements Controls {

    private final String service;
    private final History history;
    private final Dispatcher dispatcher;

    public Executor(String service, History history, Dispatcher dispatcher) {
        this.service = service;
        this.history = history;
        this.dispatcher = dispatcher;
    }

    public void push(Screen screen, String tag, Bundle extras) {
        dispatcher.dispatch(history.add(screen, service, tag, extras));
    }

//    public void replace(Screen screen, String service, String transition, String tag) {
//        check();
//
//        List<History.Entry> entries = new ArrayList<>();
//        entries.add(history.kill(null, true));
//        entries.add(history.add(screen, service, transition, tag));
//        dispatcher.dispatch(entries);
//    }

    @Override
    public boolean pop() {
        return pop(null);
    }

    @Override
    public boolean pop(Object result) {
        if (!history.canKill()) {
            return false;
        }

        dispatcher.dispatch(history.kill(result, false));
        return true;
    }

//    public boolean popToRoot() {
//        return popToRoot(null);
//    }
//
//    public boolean popToRoot(Object result) {
//        if (!architect.history.canKill()) {
//            return false;
//        }
//
//        architect.dispatcher.dispatch(architect.history.killAllButRoot(result));
//        return true;
//    }
}
