package architect.examples.simple_app.screen.home2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import architect.Screen;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@Parcel(parcelsIndex = false)
public class HomeScreen2 implements Screen {

    public HomeScreen2() {
    }

    @Override
    public View createView(Context context, ViewGroup parent) {
        return new HomeView2(context);
    }
}
