package architect.robot;

import android.view.View;

import autodagger.AutoComponent;

/**
 * Generates a Screen or ScreenPath class
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public @interface AutoScreen {

    /**
     * Generates the Dagger2 component associated to the screen
     */
    AutoComponent component();

    /**
     * Generates a ScreenPath class instead, and use the following view class
     */
    Class<? extends View> pathView() default View.class;

    /**
     * Generates a ScreenPath class instead, and use the following layout file
     */
    int pathLayout() default 0;

    /**
     * The generated screen will contain the following subscreens
     */
    ContainsSubscreen[] subScreens() default {};
}
