package architect.service.navigation;

import architect.Executor;
import architect.Screen;
import architect.service.Controller;
import architect.service.commons.EntryExtras;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigationController extends Controller {

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

    public boolean pop(Object result) {
        return executor.pop(result);
    }

    public void popToRoot() {

    }
}
