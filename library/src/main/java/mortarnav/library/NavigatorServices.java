package mortarnav.library;

import android.content.Context;

/**
 * Mortar services related to Navigator
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorServices {

    public static <T> T getService(Context context, String service) {
        //noinspection unchecked
        return (T) context.getSystemService(service);
    }

    public static ScreenContextFactory getContextFactoryService(Context context) {
        return Navigator.get(context).contextFactory;
    }
}
