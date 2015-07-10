package architect;

import mortar.MortarScope;

/**
 * Stackable is the fundation of Architect
 * It's role is to configure its associated MortarScope
 * and all what goes inside, such as a Dagger2 component
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Stackable {

    void configureScope(MortarScope.Builder builder, MortarScope parentScope);
}
