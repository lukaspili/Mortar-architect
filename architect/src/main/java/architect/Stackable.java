package architect;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Stackable {

    void configureScope(MortarScope.Builder builder, MortarScope parentScope);
}
