package architect.examples.simple_app.screen.home2;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import architect.Architect;
import architect.examples.simple_app.Architecture;
import architect.examples.simple_app.MainActivity;
import architect.examples.simple_app.R;
import architect.service.navigation.NavigationController;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeView2 extends LinearLayout {

    private static int count = 0;

    @Bind(R.id.toolbar2)
    public Toolbar toolbar;

    public HomeView2(Context context) {
        super(context);

        View view = View.inflate(context, R.layout.home2_view, this);
        ButterKnife.bind(view);

        toolbar.setTitle("Home2: " + ++count);
    }

    private Architect getArchitect() {
        return ((MainActivity) getContext()).getArchitect();
    }

    private NavigationController getShowController() {
        return getArchitect().getService(Architecture.SHOW_SERVICE).getController();
    }
}
