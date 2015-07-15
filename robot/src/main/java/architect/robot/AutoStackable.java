package architect.robot;

import android.view.View;

import autodagger.AutoComponent;

/**
 * Generates a Stackable class
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public @interface AutoStackable {

    AutoComponent component();

    /**
     * Will generate a StackablePath class instead, and use the following view class
     */
    Class<? extends View> pathWithView() default View.class;

    /**
     * Will generate a StackablePath class instead, and use the following layout file
     */
    int pathWithLayout() default 0;
}
