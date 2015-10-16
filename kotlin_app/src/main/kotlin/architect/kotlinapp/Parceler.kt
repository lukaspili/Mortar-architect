package architect.kotlinapp

import android.os.Parcelable
import architect.ScreenParceler
import architect.ScreenPath
import org.parceler.Parcels

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class Parceler : ScreenParceler<ScreenPath> {

    override fun wrap(path: ScreenPath?): Parcelable? {
        return Parcels.wrap<ScreenPath>(path)
    }

    override fun unwrap(parcelable: Parcelable?): ScreenPath? {
        return Parcels.unwrap<ScreenPath>(parcelable)
    }
}