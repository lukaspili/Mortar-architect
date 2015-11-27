package architect.hook.mortar;

import architect.hook.Hook;
import mortar.MortarScope;

/**
 * Created by lukasz on 24/11/15.
 */
public class MortarHook extends Hook {

    private final ScopeNameTracker scopeNameTracker = new ScopeNameTracker();
    private final ScopingStrategies scopingStrategies;
    private final MortarScope scope;

    public MortarHook(MortarScope scope, ScopingStrategies scopingStrategies) {
        this.scope = scope;
        this.scopingStrategies = scopingStrategies;
    }

    @Override
    public HistoryHook withHistoryHook() {
        return new MortarHistoryHook(scopeNameTracker);
    }

    @Override
    public DispatcherHook withDispatcherHook() {
        return new MortarDispatcherHook(scopeNameTracker, scope, scopingStrategies);
    }

    @Override
    public PresenterHook withPresenterHook() {
        return new MortarPresenterHook();
    }
}
