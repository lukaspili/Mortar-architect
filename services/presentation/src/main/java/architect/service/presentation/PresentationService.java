package architect.service.presentation;

import android.os.Bundle;

import architect.Controller;
import architect.Screen;
import architect.Service;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PresentationService extends Service {

    public PresentationService(String name, Controller controller) {
        super(name, controller);
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
        controller.push(screen, name, tag, bundle);
    }
}
