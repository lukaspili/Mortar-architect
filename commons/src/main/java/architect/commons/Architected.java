package architect.commons;

import architect.Navigator;
import architect.Screen;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Architected extends Screen {

    Navigator createNavigator(MortarScope scope);
}
