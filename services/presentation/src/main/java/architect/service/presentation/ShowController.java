package architect.service.presentation;

import architect.Executor;
import architect.Screen;
import architect.service.Controller;
import architect.service.commons.EntryExtras;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ShowController extends Controller {

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
}
