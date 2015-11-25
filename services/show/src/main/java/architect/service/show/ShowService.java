package architect.service.show;

import architect.Executor;
import architect.service.Delegate;
import architect.service.Registration;
import architect.service.commons.Transitions;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class ShowService implements Registration<ShowController, ShowPresenter> {

    public abstract void configureTransitions(Transitions<Transition> transitions);

    @Override
    public ShowController createController(Executor executor) {
        return new ShowController(executor);
    }

    @Override
    public ShowPresenter createPresenter() {
        Transitions<Transition> transitions = new Transitions<>();
        configureTransitions(transitions);

        return new ShowPresenter(transitions);
    }

    @Override
    public Delegate createDelegate() {
        return new ShowDelegate();
    }
}
