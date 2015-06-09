package mortarnav.library;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class Screen implements Serializable {

    public abstract View createView(Context context);

    public abstract void configureMortarScope(ScreenContextFactory.BuilderContext builderContext);

    public String getScopeName() {
        return String.format("%s_%d", getClass().getName(), System.identityHashCode(this));
    }
}
