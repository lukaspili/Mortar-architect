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

    private final Navigator navigator;
    private final SimpleArrayMap<Class, Integer> ids;

    public Scoper(Navigator navigator) {
        this.navigator = navigator;
        ids = new SimpleArrayMap<>();
    }

    public MortarScope getCurrentScope(Screen screen) {
        Class cls = screen.getClass();
        int id = getId(cls);
        String name = getName(cls, id);

        MortarScope scope = navigator.getScope().findChild(name);
        if (scope == null) {
            return createNewScope(screen, cls, id, true, 1);
        }
        return scope;
    }

    public MortarScope getNewScope(Screen screen, boolean forward, int depth) {
        Class cls = screen.getClass();
        return createNewScope(screen, cls, getId(cls), forward, depth);
    }

    private MortarScope createNewScope(Screen screen, Class cls, int currentId, boolean forward, int depth) {
        currentId = forward ? currentId + depth : currentId - depth;
        Preconditions.checkArgument(currentId > 0, "Scoper name id must be > 0");

        ids.put(cls, currentId);
        String name = getName(cls, currentId);

        Logger.d("Create scope: %s", name);
        return MortarFactory.createScope(navigator.getScope(), screen, name);
    }

    private int getId(Class cls) {
        int id;
        if (ids.containsKey(cls)) {
            id = ids.get(cls);
        } else {
            id = 0;
        }

        return id;
    }

    private String getName(Class cls, int id) {
        return String.format("ARCHITECT_SCOPE_%s_%d", cls, id);
    }
}
