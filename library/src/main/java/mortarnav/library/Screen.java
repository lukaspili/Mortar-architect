package mortarnav.library;

import android.content.Context;
import android.view.View;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class Screen {

    public abstract View createView(Context context);


    public String getScopeName() {
        return getClass().getName();
    }
}
