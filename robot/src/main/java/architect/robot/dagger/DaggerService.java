package architect.robot.dagger;

import android.content.Context;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class DaggerService {

    public static final String SERVICE_NAME = DaggerService.class.getName();

    public static void configureScope(MortarScope.Builder builder, Class type, Object component) {
        builder.withService(SERVICE_NAME, component)
                .withService(getTypedServiceName(type), component);
    }

    public static <T> T getTyped(Context context, Class type) {
        //noinspection unchecked, ResourceType
        return (T) context.getSystemService(getTypedServiceName(type));
    }

    public static <T> T getTyped(MortarScope scope, Class type) {
        //noinspection unchecked
        return (T) scope.getService(getTypedServiceName(type));
    }

    public static <T> T get(MortarScope scope) {
        //noinspection unchecked, ResourceType
        return (T) scope.getService(SERVICE_NAME);
    }

    public static <T> T get(Context context) {
        //noinspection unchecked, ResourceType
        return (T) context.getSystemService(SERVICE_NAME);
    }

    private static String getTypedServiceName(Class type) {
        return SERVICE_NAME + "_" + type.getName();
    }
}
