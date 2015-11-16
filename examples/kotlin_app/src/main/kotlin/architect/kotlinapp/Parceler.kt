package architect.kotlinapp

import android.os.Parcelable
import architect.ScreenParceler
import architect.Screen
import org.parceler.Parcels

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class Parceler : ScreenParceler<Screen> {

    override fun wrap(path: Screen?): Parcelable? {
        return Parcels.wrap<Screen>(path)
    }

    override fun unwrap(parcelable: Parcelable?): Screen? {
        return Parcels.unwrap<Screen>(parcelable)
    }
}