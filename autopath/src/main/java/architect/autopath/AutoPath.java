package architect.autopath;

import android.view.View;

/**
 * Auto path will generate path class for you
 * Annotation can be applied on navigation scope class only
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public @interface AutoPath {

    Class<? extends View> withView();
}