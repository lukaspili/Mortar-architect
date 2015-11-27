package architect.examples.simple_app.screen.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import architect.Screen;
import architect.behavior.ReceivesResult;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@Parcel(parcelsIndex = false)
public class HomeScreen implements Screen, ReceivesResult<String> {

    final String name;
    private String result;

    @ParcelConstructor
    public HomeScreen(String name) {
        this.name = name;
    }

    @Override
    public View createView(Context context, ViewGroup parent) {
        return new HomeView(context, name, result);
    }

    @Override
    public void setResult(String result) {
        this.result = result;
    }
}
