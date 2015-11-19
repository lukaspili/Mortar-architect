package architect.service.presentation;

import android.os.Bundle;

import architect.Executor;
import architect.Screen;
import architect.service.Controller;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PresentationController extends Controller {

    public PresentationController(Executor executor) {
        super(executor);
    }

    public void show(Screen screen) {
        show(screen, null, null);
    }

    public void show(Screen screen, String transition) {
        show(screen, null, transition);
    }

    public void show(Screen screen, String tag, String transition) {
        Bundle bundle = null;
        if (transition != null) {
            bundle = new Bundle();
            bundle.putString(EntryExtras.TRANSITION, transition);
        }
        executor.push(screen, tag, bundle);
    }
}
