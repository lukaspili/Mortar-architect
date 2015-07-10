package architect.commons.view;

import android.content.Context;

import architect.Stackable;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface StackedContainer {

    String getStackableIdentifier();

    Stackable getStackable();

    void initWithContext(Context context);
}
