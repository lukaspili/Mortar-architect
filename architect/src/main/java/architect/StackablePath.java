package architect;

import android.content.Context;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface StackablePath extends Stackable {

    View createView(Context context);
}
