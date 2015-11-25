//package architect;
//
///**
// * History scope names policy
// *
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//class HistoryTracker {
//
//    final static String EXCEPTION_ID_BELOW_0 = "History tracker id cannot be < 0";
//
//    private final SimpleArrayMap<Class, Integer> ids = new SimpleArrayMap<>();
//
//    int get(History.Entry entry) {
//        Class cls = entry.screen.getClass();
//        return ids.containsKey(cls) ? ids.get(cls) : 0;
//    }
//
//    void increment(History.Entry entry) {
//        update(entry, true);
//    }
//
//    void decrement(History.Entry entry) {
//        update(entry, false);
//    }
//
//    private void update(History.Entry entry, boolean increment) {
//        Class cls = entry.screen.getClass();
//        int id = ids.containsKey(cls) ? ids.get(cls) : 0;
//        int newId = id + (increment ? 1 : -1);
//
//        Preconditions.checkArgument(newId >= 0, EXCEPTION_ID_BELOW_0);
//        ids.put(cls, newId);
//    }
//}
