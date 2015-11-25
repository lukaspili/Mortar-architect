package architect.adapter;

import java.util.List;

import architect.History;
import architect.Processing;

/**
 * Created by lukasz on 25/11/15.
 */
public abstract class Hook {

    public abstract HistoryHook withHistoryHook();

    public abstract DispatcherHook withDispatcherHook();

    public interface HistoryHook {
        void onAddEntry(History.Entry entry);

        void onKillEntry(History.Entry entry);

        void onKillEntries(List<History.Entry> entries);

        void onReplaceEntry(History.Entry entry);
    }

    public interface DispatcherHook {
        void onStartDispatch(History.Entry enterEntry, History.Entry exitEntry, Processing processing);

        void onEndDispatch(History.Entry enterEntry, History.Entry exitEntry, Processing processing);
    }
}
