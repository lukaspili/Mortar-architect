package architect.service.show;

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
public class ShowController extends Controller {

    private static final Validator<List<History.Entry>> canKillValidator = new Validator<List<History.Entry>>() {
        @Override
        public boolean isValid(List<History.Entry> entries) {
            return !entries.isEmpty();
        }
    };

    public ShowController(Executor executor) {
        super(executor);
    }

    public void show(Screen screen) {
        show(screen, null, null);
    }

    public void show(Screen screen, String transition) {
        show(screen, null, transition);
    }

    public void show(Screen screen, String tag, String transition) {
        executor.push(screen, tag, EntryExtras.builder().transition(transition).toBundle());
    }

    public boolean hide() {
        return executor.pop(canKillValidator);
    }

    public boolean hide(Object result) {
        return executor.pop(canKillValidator, result);
    }

    public void hideAll() {
        executor.clear();
    }
}
