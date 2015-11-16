package architect;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * ScreenPath = Screen + Navigation params
 *
 * Screen that is used in navigation
 * It adds the meaning to create the view programmatically
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ScreenPath extends Screen {

    View createView(Context context, ViewGroup parent);
}
