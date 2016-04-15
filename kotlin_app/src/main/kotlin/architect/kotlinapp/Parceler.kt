package architect.kotlinapp

import android.os.Parcelable
import architect.ScreenParceler
import architect.ScreenPath
import nz.bradcampbell.paperparcel.PaperParcels
import nz.bradcampbell.paperparcel.TypedParcelable

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class Parceler : ScreenParceler<ScreenPath> {

    override fun wrap(path: ScreenPath?): Parcelable? {
        return PaperParcels.wrap(path)
    }

    override fun unwrap(parcelable: Parcelable?): ScreenPath? {
        return PaperParcels.unwrap(parcelable as TypedParcelable<ScreenPath>)
    }
}