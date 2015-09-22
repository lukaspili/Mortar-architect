package architect.commons.view;

import android.content.Context;

import architect.Screen;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ScreenViewContainer {

    void initWithScreenContext(Context context);

//    Context getScreenContext();

    String getScreenUniqueId();

    Screen getScreen();

}
