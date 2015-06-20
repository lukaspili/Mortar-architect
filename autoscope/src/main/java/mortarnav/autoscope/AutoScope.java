package mortarnav.autoscope;

import autodagger.AutoComponent;
import mortarnav.autopath.AutoPath;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public @interface AutoScope {
    AutoComponent component();

    AutoPath path() default @AutoPath(withView = VoidView.class);
}
