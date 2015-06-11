package mortarnav.library;

import android.content.Context;

/**
 * Mortar services related to Navigator
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorServices {

    public static ScreenContextFactory getContextFactoryService(Context context) {
        return Navigator.get(context).contextFactory;
    }
}
