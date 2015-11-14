package architect.basic;

import architect.Controller;
import architect.Screen;
import architect.Service;
import architect.ServiceDelegate;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class BasicService implements Service {

    private final String where;
    private final ServiceDelegate delegate;
    private final Controller controller = new BasicController();

    public BasicService(String where, ServiceDelegate delegate) {
        this.where = where;
        this.delegate = delegate;
    }

    public void show(Screen screen) {
        delegate.push(screen, where, null, null);
    }

    @Override
    public Controller controller() {
        return controller;
    }
}
