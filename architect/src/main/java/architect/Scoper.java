package architect;

import android.support.v4.util.SimpleArrayMap;

import mortar.MortarScope;

/**
 * Stack scope names policy
 * Tracks and provides unique names for stack scopes
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Scoper {

    public static final String SERVICE_NAME = Scoper.class.getName();

    private final Navigator navigator;
    private final SimpleArrayMap<String, Integer> ids;

    public Scoper(Navigator navigator) {
        this.navigator = navigator;
        ids = new SimpleArrayMap<>();
    }

    public MortarScope getCurrentScope(Screen screen) {
        String name = getCurrentScopeName(screen);
        MortarScope scope = navigator.getScope().findChild(name);
        if (scope == null) {
            Logger.d("Create scope: %s", name);
            scope = MortarFactory.createScope(navigator.getScope(), screen, name);
        }
        return scope;
    }

    public MortarScope getNewScope(Screen screen) {
        String name = getNewScopeName(screen);
        MortarScope scope = navigator.getScope().findChild(name);
        if (scope == null) {
            Logger.d("Create scope: %s", name);
            scope = MortarFactory.createScope(navigator.getScope(), screen, name);
        }
        return scope;
    }

    public String getCurrentScopeName(Object obj) {
        String name = obj.getClass().getName();
        int id = getId(name);
        return getName(name, id);
    }

    public String getNewScopeName(Object obj) {
        String name = obj.getClass().getName();
        int id = getId(name);
        ids.put(name, ++id);

        return getName(name, id);
    }

//    private void incrementId(Screen screen) {
//        String name = screen.getClass().getName();
//        int id;
//        if (ids.containsKey(name)) {
//            id = ids.get(name);
//        } else {
//            id = 1;
//        }
//
//        ids.put(name, id);
//    }

    private int getId(String name) {
        int id;
        if (ids.containsKey(name)) {
            id = ids.get(name);
        } else {
            id = 1;
            ids.put(name, id);
        }

        return id;
    }

    private String getName(String name, int id) {
        return String.format("ARCHITECT_SCOPE_%s_%d", name, id);
    }
}
