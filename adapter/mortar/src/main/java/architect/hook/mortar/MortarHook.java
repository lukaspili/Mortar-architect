package architect.hook.mortar;

import architect.adapter.Hook;

/**
 * Created by lukasz on 24/11/15.
 */
public class MortarHook extends Hook {

    @Override
    public HistoryHook withHistoryHook() {
        return new MortarHistoryHook();
    }

    @Override
    public DispatcherHook withDispatcherHook() {
        return new MortarDispatcherHook();
    }
}
