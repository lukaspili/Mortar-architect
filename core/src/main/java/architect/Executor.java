package architect;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import java.util.List;

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

    public boolean pop(Validator<List<History.Entry>> validator) {
        return pop(validator, null);
    }

    public boolean pop(Validator<List<History.Entry>> validator, Object result) {
        if (!history.canKill(service, validator)) {
            return false;
        }

        dispatcher.dispatch(history.kill(service, result, false));
        return true;
    }

    public boolean popUntil(Validator<List<History.Entry>> validator, int index) {
        if (!history.canKill(service, validator)) {
            return false;
        }

        dispatcher.dispatch(history.killUntil(service, index));
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
