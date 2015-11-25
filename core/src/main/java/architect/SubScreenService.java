//package architect;
//
//import android.content.Context;
//import android.view.View;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class SubScreenService {
//
//    public static final String SERVICE_NAME = SubScreenService.class.getName();
//
//    private static <T extends ArchitectedScope> Map<String, T> get(Context context) {
//        //noinspection unchecked, ResourceType
//        return (Map<String, T>) context.getSystemService(SERVICE_NAME);
//    }
//
//    public static <T extends ArchitectedScope> T get(Context context, String name) {
//        //noinspection unchecked
//        Map<String, T> screens = get(context);
//        if (screens == null) {
//            return null;
//        }
//
//        return screens.get(name);
//    }
//
//    public static <T> T get(View view, String name) {
//        //noinspection unchecked
//        return get(view.getContext(), name);
//    }
//
//    public static class Builder {
//
//        private Map<String, Object> screens = new HashMap<>();
//
//        public Builder withScreen(String name, Object screen) {
//            screens.put(name, screen);
//            return this;
//        }
//
//        public Map<String, Object> build() {
//            return screens;
//        }
//    }
//}
