package architect.service.presentation;

import android.view.ViewGroup;

import architect.Controller;
import architect.mortar.Registration;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class Presentation implements Registration<PresentationService, PresentationPresenter> {

    public abstract void configurePresenter(PresentationPresenter presenter);

    @Override
    public PresentationService createService(String name, Controller controller) {
        return new PresentationService(name, controller);
    }

    @Override
    public PresentationPresenter createPresenter(ViewGroup view) {
        return new PresentationPresenter(view);
    }
}
