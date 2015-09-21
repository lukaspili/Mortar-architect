package architect.commons;

import android.content.Context;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenService {

    public static final String SERVICE_NAME = ScreenService.class.getName();

    private static <T> Map<String, T> get(Context context) {
        //noinspection unchecked, ResourceType
        return (Map<String, T>) context.getSystemService(SERVICE_NAME);
    }

    public static <T> T get(Context context, Class cls) {
        //noinspection unchecked
        Map<String, T> screens = get(context);
        if (screens == null) {
            return null;
        }

        return screens.get(cls.getName());
    }

    public static <T> T get(View view, Class cls) {
        return get(view.getContext(), cls);
    }

    public static class Builder {

        private Map<String, Object> screens = new HashMap<>();

        public Builder withScreen(Class cls, Object screen) {
            screens.put(cls.getName(), screen);
            return this;
        }

        public Map<String, Object> build() {
            return screens;
        }
    }
}
