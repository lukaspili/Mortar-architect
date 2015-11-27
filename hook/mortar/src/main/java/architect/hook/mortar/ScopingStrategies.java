package architect.hook.mortar;

import android.support.v4.util.SimpleArrayMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukasz on 26/11/15.
 */
public class ScopingStrategies {

    private Strategy destroyInHistoryStrategy;
    private Strategy preserveInHistoryStrategy;
    private List<String> withDestroyStrategy;
    private List<String> withPreserveStrategy;

    public ScopingStrategies setDestroyInHistoryStrategyFor(String service) {
        if (withDestroyStrategy == null) {
            withDestroyStrategy = new ArrayList<>();
        }
        withDestroyStrategy.add(service);
        return this;
    }

    public ScopingStrategies setPreserveInHistoryStrategyFor(String service) {
        if (withPreserveStrategy == null) {
            withPreserveStrategy = new ArrayList<>();
        }
        withPreserveStrategy.add(service);
        return this;
    }

    Strategy get(String service) {
        if (withDestroyStrategy != null && withDestroyStrategy.contains(service)) {
            return getDestroyInHistoryStrategy();
        }

        if (withPreserveStrategy != null && withPreserveStrategy.contains(service)) {
            return getPreserveInHistoryStrategy();
        }

        throw new IllegalStateException("Missing scoping strategy for service: " + service);
    }

    private Strategy getDestroyInHistoryStrategy() {
        if (destroyInHistoryStrategy == null) {
            destroyInHistoryStrategy = new DestroyInHistoryStrategy();
        }
        return destroyInHistoryStrategy;
    }

    private Strategy getPreserveInHistoryStrategy() {
        if (preserveInHistoryStrategy == null) {
            preserveInHistoryStrategy = new PreserveInHistoryStrategy();
        }
        return preserveInHistoryStrategy;
    }

    public static class DestroyInHistoryStrategy implements Strategy {
        @Override
        public boolean buildEnterScope(boolean forward) {
            return true;
        }

        @Override
        public boolean destroyExitScope(boolean forward) {
            return true;
        }

        @Override
        public boolean buildAllScopesOnRestore() {
            return false;
        }
    }

    public static class PreserveInHistoryStrategy implements Strategy {
        @Override
        public boolean buildEnterScope(boolean forward) {
            return forward;
        }

        @Override
        public boolean destroyExitScope(boolean forward) {
            return !forward;
        }

        @Override
        public boolean buildAllScopesOnRestore() {
            return true;
        }
    }

    public interface Strategy {

        boolean buildEnterScope(boolean forward);

        boolean destroyExitScope(boolean forward);

        boolean buildAllScopesOnRestore();
    }
}
