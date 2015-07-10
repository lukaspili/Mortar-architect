package architect.commons;

import architect.Navigator;
import architect.Stackable;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Architected extends Stackable {

    Navigator createNavigator(MortarScope scope);
}
