package mortarnav.library;

import android.content.Context;

import mortar.MortarScope;
import mortarnav.library.screen.ScreenContextFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorServices {

    public static final String CONTEXTFACTORY_SERVICE_NAME = NavigatorServices.class.getName() + "_ContextFactoryService";

    public static <T> T getService(Context context, String service) {
        //noinspection unchecked
        return (T) context.getSystemService(service);
    }

    public static ScreenContextFactory getContextFactoryService(Context context) {
        return getService(context, CONTEXTFACTORY_SERVICE_NAME);
    }

    public static MortarScope.Builder buildNavigatorContainerScope(Context parentContext, Navigator navigator) {
        return MortarScope.buildChild(parentContext)
                .withService(Navigator.SERVICE_NAME, navigator)
                .withService(CONTEXTFACTORY_SERVICE_NAME, navigator.getContextFactory());
    }

}
