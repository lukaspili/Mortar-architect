package mortarnav.library;

import android.content.Context;
import android.view.View;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class Screen<T extends View> {

    public abstract T createView(Context context);

    public abstract void configureScope(MortarScope parentScope, MortarScope.Builder builder);

    public String getScopeName() {
        return getClass().getName();
    }
}
