package architect.hook.mortar;

import android.support.v4.util.SimpleArrayMap;

import java.util.List;

import architect.History;
import architect.Processing;
import architect.adapter.Hook;
import mortar.MortarScope;

/**
 * Created by lukasz on 25/11/15.
 */
class MortarDispatcherHook implements Hook.DispatcherHook {

    private final ScopeNameTracker scopeNameTracker;
    private final MortarScope rootScope;
    private final List<ScopingStrategy> scopingStrategies;

    public MortarDispatcherHook(ScopeNameTracker scopeNameTracker, MortarScope rootScope, List<ScopingStrategy> scopingStrategies) {
        this.scopeNameTracker = scopeNameTracker;
        this.rootScope = rootScope;
        this.scopingStrategies = scopingStrategies;
    }

    @Override
    public void onStartDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward, Processing processing) {
        boolean build = getScopingStrategy(exitEntry).shouldBuildEnterScopeOnStartDispatch(enterEntry, exitEntry, forward);
        processing.put("scope", build ? buildScope(enterEntry) : findScope(enterEntry));
    }

    @Override
    public void onEndDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward, Processing processing) {
        if (getScopingStrategy(exitEntry).shouldDestroyExitScopeOnEndDispatch(enterEntry, exitEntry, forward)) {
            destroyScope(exitEntry);
        }
        processing.remove("scope");
    }

    @Override
    public void onRestore(SimpleArrayMap<String, List<History.Entry>> entries) {

    }

    private ScopingStrategy getScopingStrategy(History.Entry entry) {
        ScopingStrategy strategy;
        for (int i = 0; i < scopingStrategies.size(); i++) {
            strategy = scopingStrategies.get(i);
            if (strategy.isValidStrategy(entry)) {
                return strategy;
            }
        }

        return new DefaultScopingStrategy();
    }

    private MortarScope buildScope(History.Entry entry) {
        String name = scopeNameTracker.get(entry);
        return MortarFactory.createScope(rootScope, entry.screen, name);
    }

    private void destroyScope(History.Entry entry) {
        findScope(entry).destroy();
    }

    private MortarScope findScope(History.Entry entry) {
        return rootScope.findChild(scopeNameTracker.get(entry));
    }
}