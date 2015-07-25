package architect.commons.view;

import android.content.Context;

import architect.Screen;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface StackedContainer {

    String getStackableIdentifier();

    Screen getStackable();

    void initWithContext(Context context);
}
