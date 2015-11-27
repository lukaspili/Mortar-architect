package architect;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import architect.hook.Hook;

/**
 * Created by lukasz on 25/11/15.
 */
public class Hooks {

    private final List<Hook.HistoryHook> historyHooks;
    private final List<Hook.DispatcherHook> dispatcherHooks;
    private final PresenterHooks presenterHooks;

    Hooks() {
        this(new ArrayList<Hook.HistoryHook>(), new ArrayList<Hook.DispatcherHook>(), new PresenterHooks());
    }

    Hooks(List<Hook.HistoryHook> historyHooks, List<Hook.DispatcherHook> dispatcherHooks, PresenterHooks presenterHooks) {
        this.historyHooks = historyHooks;
        this.dispatcherHooks = dispatcherHooks;
        this.presenterHooks = presenterHooks;
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

        Hook.PresenterHook presenterHook = hook.withPresenterHook();
        if (presenterHook != null) {
            presenterHooks.hooks.add(presenterHook);
        }
    }

    public void hookHistory(HookOn<Hook.HistoryHook> hookOn) {
        if (historyHooks.isEmpty()) {
            return;
        }

        for (int i = 0; i < historyHooks.size(); i++) {
            hookOn.hook(historyHooks.get(i));
        }
    }

    public void hookDispather(HookOn<Hook.DispatcherHook> hookOn) {
        if (dispatcherHooks.isEmpty()) {
            return;
        }

        for (int i = 0; i < dispatcherHooks.size(); i++) {
            hookOn.hook(dispatcherHooks.get(i));
        }
    }

    public PresenterHooks getPresenterHooks() {
        return presenterHooks;
    }

    public static class PresenterHooks implements Hook.PresenterHook {

        private final List<Hook.PresenterHook> hooks;

        PresenterHooks() {
            this(new ArrayList<Hook.PresenterHook>());
        }

        PresenterHooks(List<Hook.PresenterHook> hooks) {
            this.hooks = hooks;
        }

        @Override
        public Context getOverridedContext(View containerView, History.Entry entry, Processing processing) {
            Context context;
            for (int i = 0; i < hooks.size(); i++) {
                context = hooks.get(i).getOverridedContext(containerView, entry, processing);
                if (context != null) {
                    return context;
                }
            }

            return null;
        }
    }

    public interface HookOn<T> {
        void hook(T on);
    }
}
