package architect.examples.mortar_app;

import android.os.Parcelable;

import org.parceler.Parcels;

import architect.Screen;
import architect.ScreenParceler;

/**
 * PathParceler that uses Parceler library
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Parceler implements ScreenParceler {

    @Override
    public Parcelable wrap(Screen path) {
        return Parcels.wrap(path);
    }

    @Override
    public Screen unwrap(Parcelable parcelable) {
        return Parcels.unwrap(parcelable);
    }
}
