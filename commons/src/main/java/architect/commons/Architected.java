package architect.commons;

import architect.Architect;
import architect.ArchitectedScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Architected extends ArchitectedScope {

    Architect createNavigator();
}
