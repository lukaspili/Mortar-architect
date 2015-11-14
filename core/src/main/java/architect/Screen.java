package architect;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import mortar.MortarScope;

/**
 * ScreenPath = Screen + Navigation params
 *
 * Screen that is used in navigation
 * It allows to create the view programmatically
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Screen extends ArchitectedScope {

    void configureScope(MortarScope.Builder builder, MortarScope parentScope);

    View createView(Context context, ViewGroup parent);
}
