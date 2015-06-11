package mortarnav.library.dagger;

import mortarnav.library.Transitions;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface NavigatorInjector {

    void inject(Transitions transitions);
}
