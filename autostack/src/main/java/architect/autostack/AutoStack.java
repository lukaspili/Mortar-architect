package architect.autostack;

import autodagger.AutoComponent;
import architect.autopath.AutoPath;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public @interface AutoStack {
    AutoComponent component();

    AutoPath path() default @AutoPath(withView = VoidView.class);
}
