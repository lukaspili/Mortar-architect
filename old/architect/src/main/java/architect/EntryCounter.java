package architect;

/**
 * History scope names policy
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class EntryCounter {

    private final SimpleArrayMap<Class, Integer> ids = new SimpleArrayMap<>();

    int get(History.Entry entry) {
        Class cls = entry.path.getClass();
        return ids.containsKey(cls) ? ids.get(cls) : 0;
    }

    void increment(History.Entry entry) {
        update(entry, true);
    }

    void decrement(History.Entry entry) {
        update(entry, false);
    }

    private void update(History.Entry entry, boolean increment) {
        Class cls = entry.path.getClass();
        int id = ids.containsKey(cls) ? ids.get(cls) : 0;
        ids.put(cls, id + (increment ? 1 : -1));

        Logger.d("Scoper update count for %s = %d", cls.getName(), ids.get(cls));
    }
}
