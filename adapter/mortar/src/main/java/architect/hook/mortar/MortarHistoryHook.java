package architect.hook.mortar;

import java.util.List;

import architect.History;
import architect.adapter.Hook;

/**
 * Created by lukasz on 25/11/15.
 */
public class MortarHistoryHook implements Hook.HistoryHook {

    @Override
    public void onAddEntry(History.Entry entry) {

    }

    @Override
    public void onKillEntry(History.Entry entry) {

    }

    @Override
    public void onKillEntries(List<History.Entry> entries) {

    }

    @Override
    public void onReplaceEntry(History.Entry entry) {
        // when replacing, we won't decrement the scope of the exit entry because
        // it can create conflict between enter and exit scope names during dispatch
        // => do nothing
    }
}
