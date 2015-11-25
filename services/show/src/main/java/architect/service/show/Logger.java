package architect.service.show;

import android.util.Log;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class Logger {

    private static final boolean DEBUG = true;

    static void d(String message, Object... format) {
        if (!DEBUG) return;

        if (format != null) {
            message = String.format(message, format);
        }

        Log.d("Navigator Presentation", message);
    }
}
