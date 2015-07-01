package architect.commons.view;

import android.content.Context;

import architect.StackScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface StackedContainer {

    String getScopeIdentifier();

    StackScope getScope();

    void initWithContext(Context context);
}
