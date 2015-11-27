package architect.hook.mortar;

import android.support.v4.util.SimpleArrayMap;

import architect.History;
import architect.Screen;

/**
 * History scope names policy
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class ScopeNameTracker {

    final static String EXCEPTION_ID_BELOW_0 = "History tracker id cannot be < 0";

    private final SimpleArrayMap<Class, Integer> lastIds = new SimpleArrayMap<>();
    private final SimpleArrayMap<Screen, Integer> ids = new SimpleArrayMap<>();

    String get(History.Entry entry) {
        Preconditions.checkArgument(ids.containsKey(entry.screen), "ScopeNameTracker does not contain this screen: %s", entry.screen);
        return String.format("ARCHITECT_MORTAR_SCOPE_%s_%s", entry.screen.toString(), ids.get(entry.screen));
    }

    void increment(History.Entry entry) {
        update(entry, true);
    }

    void decrement(History.Entry entry) {
        update(entry, false);
    }

    void remove(History.Entry entry) {
        Preconditions.checkArgument(ids.containsKey(entry.screen), "Screen to remove not found");
        ids.remove(entry.screen);
    }

    private void update(History.Entry entry, boolean increment) {
        Class cls = entry.screen.getClass();
        int id = lastIds.containsKey(cls) ? lastIds.get(cls) : 0;
        int newId = id + (increment ? 1 : -1);

        if (increment) {
            Preconditions.checkArgument(!ids.containsKey(entry.screen), "Screen cannot be already tracked");
            ids.put(entry.screen, id);
        }

        Preconditions.checkArgument(newId >= 0, EXCEPTION_ID_BELOW_0);
        lastIds.put(cls, newId);
    }
}
