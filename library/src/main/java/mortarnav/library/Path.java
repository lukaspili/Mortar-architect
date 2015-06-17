package mortarnav.library;

import android.view.View;

/**
 * Annotation processor will generate path class for you
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public @interface Path {

    Class<? extends View> withView();
}
