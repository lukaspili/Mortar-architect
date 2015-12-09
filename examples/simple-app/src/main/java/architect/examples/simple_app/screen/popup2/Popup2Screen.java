package architect.examples.simple_app.screen.popup2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import architect.Screen;

/**
 * Created by lukasz on 09/12/15.
 */

@Parcel(parcelsIndex = false)
public class Popup2Screen implements Screen {

    String title;

    @ParcelConstructor
    public Popup2Screen(String title) {
        this.title = title;
    }

    @Override
    public View createView(Context context, ViewGroup parent) {
        return new Popup2View(context, title);
    }
}
