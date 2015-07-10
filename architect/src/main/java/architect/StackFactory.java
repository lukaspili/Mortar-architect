package architect;

import android.content.Context;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class StackFactory {

    public static Context createContext(Context parentContext, Stackable stackable) {
        return createContext(parentContext, stackable, null);
    }

    public static Context createContext(Context parentContext, Stackable stackable, String identifier) {
        Preconditions.checkNotNull(parentContext, "Parent context cannot be null");
        Preconditions.checkNotNull(stackable, "Stackable cannot be null");

        MortarScope parentScope = MortarScope.getScope(parentContext);
        Preconditions.checkNotNull(parentScope, "Parent scope cannot be null");

        String scopeName = identifier != null ? String.format("%s_%s", stackable.getClass().getName(), identifier) : stackable.getClass().getName();
        MortarScope mortarScope = parentScope.findChild(scopeName);
        if (mortarScope == null) {
            mortarScope = createScope(parentScope, stackable, scopeName);
        }

        return mortarScope.createContext(parentContext);
    }

//    /**
//     * Create Mortar scope from stack scope
//     */
//    static MortarScope createScope(MortarScope parentScope, Stackable stackable, String scopeName) {
//        MortarScope.Builder scopeBuilder = parentScope.buildChild();
//
//        StackScope.Services services = stackable.withServices(parentScope);
//        if (services != null && !services.services.isEmpty()) {
//            for (Map.Entry<String, Object> service : services.services.entrySet()) {
//                scopeBuilder.withService(service.getKey(), service.getValue());
//            }
//        }
//
//        return scopeBuilder.build(scopeName);
//    }

    /**
     * Create Mortar scope from stackable
     */
    static MortarScope createScope(MortarScope parentScope, Stackable stackable, String scopeName) {
        MortarScope.Builder scopeBuilder = parentScope.buildChild();
        stackable.configureScope(scopeBuilder, parentScope);
        return scopeBuilder.build(scopeName);
    }
}
