package architect.robot;

import android.content.Context;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class RobotService {

//    public static final String SERVICE_NAME = RobotService.class.getName();

    public static final String DAGGER_SERVICE_NAME = RobotService.class.getName() + "_DAGGER";

    public static <T> T get(Context context, String name) {
        //noinspection unchecked, ResourceType
        return (T) context.getSystemService(name);
    }

    public static <T> T getDaggerService(Context context) {
        return get(context, DAGGER_SERVICE_NAME);
    }

//    public static <T> T get(Context context) {
//        //noinspection unchecked, ResourceType
//        return (T) context.getSystemService(SERVICE_NAME);
//    }
}
