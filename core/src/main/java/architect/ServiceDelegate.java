package architect;

import android.os.Bundle;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ServiceDelegate {

    private final Architect architect;

    public ServiceDelegate(Architect architect) {
        this.architect = architect;
    }

    public void push(Screen screen, String service, String tag, Bundle extras) {
        architect.dispatcher.dispatch(architect.history.add(screen, service, tag, extras));
    }

//    public void replace(Screen screen, String service, String transition, String tag) {
//        check();
//
//        List<History.Entry> entries = new ArrayList<>();
//        entries.add(history.kill(null, true));
//        entries.add(history.add(screen, service, transition, tag));
//        dispatcher.dispatch(entries);
//    }

    public boolean pop() {
        return pop(null);
    }

    public boolean pop(Object result) {
        if (!architect.history.canKill()) {
            return false;
        }

        architect.dispatcher.dispatch(architect.history.kill(result, false));
        return true;
    }

    public boolean popToRoot() {
        return popToRoot(null);
    }

    public boolean popToRoot(Object result) {
        if (!architect.history.canKill()) {
            return false;
        }

        architect.dispatcher.dispatch(architect.history.killAllButRoot(result));
        return true;
    }
}
