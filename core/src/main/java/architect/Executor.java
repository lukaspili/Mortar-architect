package architect;

import android.os.Bundle;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Executor {

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

    public boolean popAnyService() {
        return pop(null, null);
    }

    public boolean pop() {
        return pop(null);
    }

    public boolean pop(Object result) {
        return pop(service, result);
    }

    private boolean pop(String service, Object result) {
        if (!history.canKill(service)) {
            return false;
        }

        dispatcher.dispatch(history.kill(result, false));
        return true;
    }

    public void clear() {
        dispatcher.dispatch(history.killAll(service));
    }

//    public boolean popTo() {
//        return popToRoot(null);
//    }
//
//    public boolean popToRoot(Object result) {
//        if (!history.canKill()) {
//            return false;
//        }
//
//        dispatcher.dispatch(history.killAllButRoot(result));
//        return true;
//    }
}
