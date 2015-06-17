package mortarnav.library;

import android.content.Context;

import java.util.Map;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigationScopeFactory {

    public static Context createContext(Context parentContext, NavigationScope scope) {
        return createContext(parentContext, scope, null);
    }

    public static Context createContext(Context parentContext, NavigationScope scope, String identifier) {
        Preconditions.checkNotNull(parentContext, "Parent context cannot be null");
        Preconditions.checkNotNull(scope, "Scope cannot be null");

        MortarScope parentScope = MortarScope.getScope(parentContext);
        Preconditions.checkNotNull(parentScope, "Parent scope cannot be null");

        String scopeName = identifier != null ? String.format("%s_%s", scope.getClass().getName(), identifier) : scope.getClass().getName();
        MortarScope mortarScope = parentScope.findChild(scopeName);
        if (mortarScope == null) {
            mortarScope = createScope(parentScope, scope, scopeName);
        }

        return mortarScope.createContext(parentContext);
    }

    /**
     * Create Mortar scope from navigation scope
     */
    static MortarScope createScope(MortarScope parentScope, NavigationScope scope, String scopeName) {
        MortarScope.Builder scopeBuilder = parentScope.buildChild();

        NavigationScope.Services services = scope.withServices(parentScope);
        if (services != null && !services.services.isEmpty()) {
            for (Map.Entry<String, Object> service : services.services.entrySet()) {
                scopeBuilder.withService(service.getKey(), service.getValue());
            }
        }

        return scopeBuilder.build(scopeName);
    }
}
