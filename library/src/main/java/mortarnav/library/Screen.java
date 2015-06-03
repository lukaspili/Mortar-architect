package mortarnav.library;

import android.content.Context;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class Screen<T extends View> {

    public abstract T withView(Context context);
}
