package architect;

import java.util.ArrayList;
import java.util.List;

import architect.adapter.Hook;

/**
 * Created by lukasz on 25/11/15.
 */
class Hooks {

    private final List<Hook.HistoryHook> historyHooks;
    private final List<Hook.DispatcherHook> dispatcherHooks;

    Hooks() {
        this(new ArrayList<Hook.HistoryHook>(), new ArrayList<Hook.DispatcherHook>());
    }

    public Hooks(List<Hook.HistoryHook> historyHooks, List<Hook.DispatcherHook> dispatcherHooks) {
        this.historyHooks = historyHooks;
        this.dispatcherHooks = dispatcherHooks;
    }

    void add(Hook hook) {
        Hook.HistoryHook historyHook = hook.withHistoryHook();
        if (historyHook != null) {
            historyHooks.add(historyHook);
        }

        Hook.DispatcherHook dispatcherHook = hook.withDispatcherHook();
        if (dispatcherHook != null) {
            dispatcherHooks.add(dispatcherHook);
        }
    }

    void hookHistory(HookOn<Hook.HistoryHook> hookOn) {
        if (historyHooks.isEmpty()) {
            return;
        }

        for (int i = 0; i < historyHooks.size(); i++) {
            hookOn.hook(historyHooks.get(i));
        }
    }

    void hookDispather(HookOn<Hook.DispatcherHook> hookOn) {
        if (dispatcherHooks.isEmpty()) {
            return;
        }

        for (int i = 0; i < dispatcherHooks.size(); i++) {
            hookOn.hook(dispatcherHooks.get(i));
        }
    }

    interface HookOn<T> {
        void hook(T on);
    }
}
