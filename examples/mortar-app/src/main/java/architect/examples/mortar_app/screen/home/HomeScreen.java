package architect.examples.mortar_app.screen.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import architect.Screen;
import architect.hook.mortar.HandlesMortarScope;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@Parcel(parcelsIndex = false)
public class HomeScreen implements Screen, HandlesMortarScope {

    final String name;

    @ParcelConstructor
    public HomeScreen(String name) {
        this.name = name;
    }

    @Override
    public View createView(Context context, ViewGroup parent) {
        return new HomeView(context, name);
    }

    @Override
    public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
        builder.withService("presenter", new HomePresenter());
    }
}
