package architect.hook.mortar;

import android.support.v4.util.SimpleArrayMap;

import java.util.List;

import architect.History;
import architect.Processing;
import architect.hook.Hook;
import mortar.MortarScope;

/**
 * Created by lukasz on 25/11/15.
 */
class MortarDispatcherHook implements Hook.DispatcherHook {

    private final ScopeNameTracker scopeNameTracker;
    private final MortarScope rootScope;
    private final ScopingStrategies scopingStrategies;

    public MortarDispatcherHook(ScopeNameTracker scopeNameTracker, MortarScope rootScope, ScopingStrategies scopingStrategies) {
        this.scopeNameTracker = scopeNameTracker;
        this.rootScope = rootScope;
        this.scopingStrategies = scopingStrategies;
    }

    @Override
    public void onStartDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward, Processing processing) {
        boolean build = scopingStrategies.get(enterEntry.service).buildEnterScope(forward);
        MortarProcessing.putSingleScope(processing, build ? buildScope(enterEntry) : findScope(enterEntry));
    }

    @Override
    public void onEndDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward, Processing processing) {
        if (scopingStrategies.get(exitEntry.service).destroyExitScope(forward)) {
            findScope(exitEntry).destroy();
            scopeNameTracker.remove(exitEntry);
        }
        MortarProcessing.clear(processing);
    }

    @Override
    public void onStartRestore(SimpleArrayMap<String, List<History.Entry>> servicesEntries, Processing processing) {
        ScopingStrategies.Strategy strategy;
        List<History.Entry> entries;
        History.Entry entry;
        for (int i = 0; i < servicesEntries.size(); i++) {
            strategy = scopingStrategies.get(servicesEntries.keyAt(i));
            entries = servicesEntries.valueAt(i);
            if (strategy.buildAllScopesOnRestore() && entries.size() > 1) {
                for (int j = 0; j < entries.size(); j++) {
                    entry = entries.get(j);
                    MortarProcessing.putScope(processing, entry, buildScope(entry));
                }
            } else {
                MortarProcessing.putSingleScope(processing, buildScope(entries.get(entries.size() - 1)));
            }
        }
    }

    @Override
    public void onEndRestore(Processing processing) {
        MortarProcessing.clear(processing);
    }

    private MortarScope buildScope(History.Entry entry) {
        String name = scopeNameTracker.get(entry);
        return MortarFactory.createScope(rootScope, entry.screen, name);
    }

    private MortarScope findScope(History.Entry entry) {
        return rootScope.findChild(scopeNameTracker.get(entry));
    }
}