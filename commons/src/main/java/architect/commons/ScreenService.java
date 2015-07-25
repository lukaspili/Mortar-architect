package architect.commons;

import android.content.Context;
import android.view.View;

import architect.commons.view.ScreenViewContainer;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenService {

    public static final String SERVICE_NAME = ScreenService.class.getName();

    public static <T> T get(Context context) {
        //noinspection unchecked, ResourceType
        return (T) context.getSystemService(SERVICE_NAME);
    }

    public static <T> T get(View view) {
        return get(view.getContext());
    }
}
