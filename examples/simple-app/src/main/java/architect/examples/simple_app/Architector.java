package architect.examples.simple_app;

import android.view.View;

import architect.Architect;
import architect.service.navigation.NavigationController;
import architect.service.show.ShowController;

/**
 * Created by lukasz on 09/12/15.
 */
public final class Architector {

    private static Architect getArchitect(View view) {
        return ((MainActivity) view.getContext()).getArchitect();
    }

    public static ShowController getShowController(View view) {
        return getArchitect(view).getService(Architecture.SHOW_SERVICE).getController();
    }

    public static NavigationController getNavigationController(View view) {
        return getArchitect(view).getService(Architecture.NAVIGATION_SERVICE).getController();
    }
}
