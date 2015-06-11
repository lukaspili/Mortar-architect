package mortarnav.library;

import android.content.Context;

import mortar.MortarScope;
import mortarnav.library.screen.Screen;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenContextFactory {

    public Context setUp(Context parentContext, Screen screen) {
        return setUp(MortarScope.getScope(parentContext), parentContext, screen);
    }

    public Context setUp(MortarScope parentScope, Context parentContext, Screen screen) {
        Preconditions.checkNotNull(parentScope, "Parent scope cannot be null");
        Preconditions.checkNotNull(parentContext, "Parent context cannot be null");
        Preconditions.checkNotNull(screen, "Screen cannot be null");

        MortarScope scope = parentScope.findChild(screen.getMortarScopeName());
        if (scope == null) {
            MortarScope.Builder scopeBuilder = parentScope.buildChild();

            // allow screen to configure its mortar scope
            BuilderContext builderContext = new BuilderContext(parentContext, parentScope, scopeBuilder);
            screen.configureMortarScope(builderContext);

            scope = scopeBuilder.build(screen.getMortarScopeName());
        }

        return scope.createContext(parentContext);
    }

    public static class BuilderContext {
        private final Context parentContext;
        private final MortarScope parentScope;
        private final MortarScope.Builder scopeBuilder;

        public BuilderContext(Context parentContext, MortarScope parentScope, MortarScope.Builder scopeBuilder) {
            this.parentContext = parentContext;
            this.parentScope = parentScope;
            this.scopeBuilder = scopeBuilder;
        }

        public Context getParentContext() {
            return parentContext;
        }

        public MortarScope getParentScope() {
            return parentScope;
        }

        public MortarScope.Builder getScopeBuilder() {
            return scopeBuilder;
        }
    }
}
