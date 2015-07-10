package architect.robot;

import android.view.View;

import autodagger.AutoComponent;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public @interface AutoStackable {

    AutoComponent component();

    Class<? extends View> pathWithView() default View.class;
}
