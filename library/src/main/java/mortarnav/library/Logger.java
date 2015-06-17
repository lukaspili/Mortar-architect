package mortarnav.library;

import android.util.Log;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class Logger {

    public static void d(String message, Object... format) {
        if (format != null) {
            message = String.format(message, format);
        }

        Log.d("Navigator", message);
    }
}
