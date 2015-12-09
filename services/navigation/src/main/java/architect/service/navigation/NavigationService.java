package architect.service.navigation;

import architect.Executor;
import architect.Hooks;
import architect.service.Delegate;
import architect.service.Registration;
import architect.service.commons.Transitions;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class NavigationService implements Registration<NavigationController, NavigationPresenter> {

    public abstract void registerTransitions(Transitions<NavigationTransition> transitions);

    @Override
    public NavigationController createController(Executor executor) {
        return new NavigationController(executor);
    }

    @Override
    public NavigationPresenter createPresenter(Hooks hooks) {
        Transitions<NavigationTransition> transitions = new Transitions<>();
        registerTransitions(transitions);

        return new NavigationPresenter(hooks, transitions);
    }

    @Override
    public Delegate createDelegate() {
        return new NavigationDelegate();
    }
}
