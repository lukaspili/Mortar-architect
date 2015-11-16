package architect;

import mortar.MortarScope;

/**
 * Screen is:
 * - MortarScope (+ whatever you put inside, such as Dagger2 component)
 * - ViewPresenter (+ ViewPresenter state)
 * - View
 *
 * Use screen in your views, or use ScreenPath with Navigator
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Screen {

    void configureScope(MortarScope.Builder builder, MortarScope parentScope);

}
