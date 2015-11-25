package architect.hook.mortar;

import mortar.MortarScope;

/**
 * Created by lukasz on 25/11/15.
 */
public interface HandlesMortarScope {

    void configureScope(MortarScope.Builder builder, MortarScope parentScope);
}
