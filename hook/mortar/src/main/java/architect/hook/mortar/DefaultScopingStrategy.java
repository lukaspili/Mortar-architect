package architect.hook.mortar;

import architect.History;

/**
 * Created by lukasz on 25/11/15.
 */
class DefaultScopingStrategy implements ScopingStrategy {

    @Override
    public boolean isValidStrategy(History.Entry entry) {
        throw new IllegalStateException("Default strategy is never checked for validity, it's only used as fallback when none is found");
    }

    @Override
    public boolean shouldBuildEnterScopeOnStartDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward) {
        return true;
    }

    @Override
    public boolean shouldDestroyExitScopeOnEndDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward) {
        return true;
    }
}
