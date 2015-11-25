package architect.hook.mortar;

import android.content.Context;

import architect.Screen;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class MortarFactory {

    public static Context createContext(Context parentContext, Screen screen) {
        return createContext(parentContext, screen, null);
    }

    public static Context createContext(Context parentContext, Screen screen, String identifier) {
        Preconditions.checkNotNull(parentContext, "Parent context cannot be null");
        Preconditions.checkNotNull(screen, "Screen cannot be null");

        MortarScope parentScope = MortarScope.getScope(parentContext);
        Preconditions.checkNotNull(parentScope, "Parent scope cannot be null");

        String scopeName = identifier != null ? String.format("%s_%s", screen.getClass().getName(), identifier) : screen.getClass().getName();
        MortarScope mortarScope = parentScope.findChild(scopeName);
        if (mortarScope == null) {
            mortarScope = createScope(parentScope, screen, scopeName);
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

//    /**
//     * Create Mortar scope from screen
//     */
//    static MortarScope createScope(MortarScope parentScope, Screen screen) {
//        Scoper scoper = parentScope.getService(Scoper.SERVICE_NAME);
//        return createScope(parentScope, screen, scoper.getNextScope(screen));
//    }

    /**
     * Create Mortar scope from screen
     */
    static MortarScope createScope(MortarScope parentScope, Screen screen, String name) {
        Preconditions.checkArgument(screen instanceof HandlesMortarScope, "Screen must implement HandlesMortarScope");

        MortarScope.Builder scopeBuilder = parentScope.buildChild();
        ((HandlesMortarScope) screen).configureScope(scopeBuilder, parentScope);
        return scopeBuilder.build(name);
    }
}
