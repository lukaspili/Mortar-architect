package architect.service.navigation;

import java.util.List;

import architect.Executor;
import architect.History;
import architect.Screen;
import architect.Validator;
import architect.service.Controller;
import architect.service.commons.EntryExtras;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigationController extends Controller {

    private static final Validator<List<History.Entry>> canKillValidator = new Validator<List<History.Entry>>() {
        @Override
        public boolean isValid(List<History.Entry> entries) {
            return entries.size() > 1;
        }
    };

    public NavigationController(Executor executor) {
        super(executor);
    }

    public void push(Screen screen) {
        push(screen, null, null);
    }

    public void push(Screen screen, String transition) {
        push(screen, null, transition);
    }

    public void push(Screen screen, String tag, String transition) {
        executor.push(screen, tag, EntryExtras.builder().transition(transition).toBundle());
    }

    public boolean pop() {
        return pop(null);
    }

    public boolean pop(Object result) {
        return executor.pop(canKillValidator, result);
    }

    public void popToRoot() {
        executor.popUntil(canKillValidator, 0);
    }
}
