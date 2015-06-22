package architect.autostack;

import android.content.Context;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class DaggerService {

    public static final String SERVICE_NAME = DaggerService.class.getName();

    public static <T> T get(Context context) {
        //noinspection unchecked, ResourceType
        return (T) context.getSystemService(SERVICE_NAME);
    }
}
