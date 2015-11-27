package architect.hook;

import android.content.Context;
import android.support.v4.util.SimpleArrayMap;
import android.view.View;

import java.util.List;

import architect.History;
import architect.Processing;

/**
 * Created by lukasz on 25/11/15.
 */
public abstract class Hook {

    public abstract HistoryHook withHistoryHook();

    public abstract DispatcherHook withDispatcherHook();

    public abstract PresenterHook withPresenterHook();

    public interface HistoryHook {
        void onAddEntry(History.Entry entry);

        void onKillEntry(History.Entry entry);

        /**
         * @param entries the killed entries in descending order (first killed on top)
         */
        void onKillEntries(List<History.Entry> entries);

        void onReplaceEntry(History.Entry entry);
    }

    public interface DispatcherHook {
        void onStartDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward, Processing processing);

        void onEndDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward, Processing processing);

        void onStartRestore(SimpleArrayMap<String, List<History.Entry>> servicesEntries, Processing processing);

        void onEndRestore(Processing processing);
    }

    public interface PresenterHook {

        Context getOverridedContext(View containerView, History.Entry entry, Processing processing);
    }
}
