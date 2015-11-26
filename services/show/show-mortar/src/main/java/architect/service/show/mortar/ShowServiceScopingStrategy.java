package architect.service.show.mortar;

import java.util.Arrays;
import java.util.List;

import architect.History;
import architect.hook.mortar.ScopingStrategy;

/**
 * Created by lukasz on 25/11/15.
 */
public class ShowServiceScopingStrategy implements ScopingStrategy {

    private final List<String> serviceNames;

    public ShowServiceScopingStrategy(String... serviceNames) {
        this.serviceNames = Arrays.asList(serviceNames);
    }

    @Override
    public boolean isValidStrategy(History.Entry entry) {
        return serviceNames.contains(entry.service);
    }

    @Override
    public boolean shouldBuildEnterScopeOnStartDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward) {
        return forward;
    }

    @Override
    public boolean shouldDestroyExitScopeOnEndDispatch(History.Entry enterEntry, History.Entry exitEntry, boolean forward) {
        return !forward;
    }
}
