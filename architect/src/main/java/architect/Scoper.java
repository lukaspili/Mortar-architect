package architect;

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
            Logger.d("Create scope: %s", name);
            scope = MortarFactory.createScope(navigator.getScope(), screen, name);
        }
        return scope;
    }

    private int getId(Class cls) {
        int id;
        if (ids.containsKey(cls)) {
            id = ids.get(cls);
        } else {
            id = 1;
            ids.put(cls, 1);
        }

        return id;
    }

    private String getName(Class cls, int id) {
        return String.format("ARCHITECT_SCOPE_%s_%d", cls, id);
    }

    public void increment(Screen screen) {
        update(screen, true);
    }

    public void decrement(Screen screen) {
        update(screen, false);
    }

    private void update(Screen screen, boolean increment) {
        Class cls = screen.getClass();
        int id = ids.containsKey(cls) ? ids.get(cls) : 0;
        ids.put(cls, id + (increment ? 1 : -1));

        Logger.d("Scoper update scope count for %s = %d", cls.getSimpleName(), ids.get(cls));
    }
}
