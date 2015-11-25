package architect.hook.mortar;

import architect.History;

/**
 * Created by lukasz on 25/11/15.
 */
public interface ScopingStrategy {

    boolean isValidStrategy(History.Entry entry);

    boolean shouldBuildEnterScopeOnStartDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward);

    boolean shouldDestroyExitScopeOnEndDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward);
}
