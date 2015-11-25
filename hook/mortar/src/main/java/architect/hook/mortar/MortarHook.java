package architect.hook.mortar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import architect.adapter.Hook;
import mortar.MortarScope;

/**
 * Created by lukasz on 24/11/15.
 */
public class MortarHook extends Hook {

    private final ScopeNameTracker scopeNameTracker = new ScopeNameTracker();
    private final List<ScopingStrategy> scopingStrategies;
    private final MortarScope scope;

    public MortarHook(MortarScope scope, ScopingStrategy... strategies) {
        this.scope = scope;
        this.scopingStrategies = strategies != null && strategies.length > 0 ? Arrays.asList(strategies) : new ArrayList<ScopingStrategy>();
    }

    @Override
    public HistoryHook withHistoryHook() {
        return new MortarHistoryHook(scopeNameTracker);
    }

    @Override
    public DispatcherHook withDispatcherHook() {
        return new MortarDispatcherHook(scopeNameTracker, scope, scopingStrategies);
    }
}
