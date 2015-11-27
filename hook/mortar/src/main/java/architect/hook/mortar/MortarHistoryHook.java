package architect.hook.mortar;

import java.util.List;

import architect.History;
import architect.hook.Hook;

/**
 * Created by lukasz on 25/11/15.
 */
class MortarHistoryHook implements Hook.HistoryHook {

    private final ScopeNameTracker scopeNameTracker;

    public MortarHistoryHook(ScopeNameTracker scopeNameTracker) {
        this.scopeNameTracker = scopeNameTracker;
    }

    @Override
    public void onAddEntry(History.Entry entry) {
        scopeNameTracker.increment(entry);
    }

    @Override
    public void onKillEntry(History.Entry entry) {
        scopeNameTracker.decrement(entry);
    }

    @Override
    public void onKillEntries(List<History.Entry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            scopeNameTracker.decrement(entries.get(i));
        }
    }

    @Override
    public void onReplaceEntry(History.Entry entry) {
        // when replacing, we won't decrement the scope of the exit entry because
        // it can create conflict between enter and exit scope names during dispatch
        // => do nothing
    }
}
