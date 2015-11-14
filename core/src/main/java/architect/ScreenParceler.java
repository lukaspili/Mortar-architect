package architect;

import android.os.Parcelable;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ScreenParceler<T extends Screen> {

    Parcelable wrap(T path);

    T unwrap(Parcelable parcelable);
}
