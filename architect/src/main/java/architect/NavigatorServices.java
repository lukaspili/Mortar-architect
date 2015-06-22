package architect;

import mortar.MortarScope;

/**
 * Mortar services related to Navigator
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorServices {

    /**
     * Get service from navigator's mortar scope or its parent
     */
    public static <T> T getService(MortarScope scope, String name) {
        Navigator navigator = scope.getService(Navigator.SERVICE_NAME);
        Preconditions.checkNotNull(navigator, "Cannot find navigator in the scope");

        return navigator.getScope().getService(name);
    }
}
