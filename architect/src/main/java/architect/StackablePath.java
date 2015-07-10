package architect;

import android.content.Context;
import android.view.View;

/**
 * Extension of Stackable that can create the associated view programmatically
 * Required by Navigator
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface StackablePath extends Stackable {

    View createView(Context context);
}
