package architect.commons;

import architect.Architect;
import architect.Screen;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Architected extends Screen {

    Architect createNavigator();
}
