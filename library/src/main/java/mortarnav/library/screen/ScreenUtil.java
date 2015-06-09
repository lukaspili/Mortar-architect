package mortarnav.library.screen;

import android.content.Context;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenUtil {

    public static <T> T getService(Context context, String service) {
        //noinspection unchecked
        return (T) context.getSystemService(service);
    }
}
