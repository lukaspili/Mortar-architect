package architect.examples.simple_app.screen.home;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import architect.Architect;
import architect.examples.simple_app.Architecture;
import architect.examples.simple_app.MainActivity;
import architect.examples.simple_app.R;
import architect.examples.simple_app.screen.home2.HomeScreen2;
import architect.service.show.ShowController;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeView extends LinearLayout {

    private static int count = 0;

    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    public HomeView(Context context, String name, String result) {
        super(context);

        View view = View.inflate(context, R.layout.home_view, this);
        ButterKnife.bind(view);

        String title = "Home: " + name;
        if (result != null) {
            title = title + " + res= " + result;
        }
        toolbar.setTitle(title);
    }

    private Architect getArchitect() {
        return ((MainActivity) getContext()).getArchitect();
    }

    private ShowController getShowController() {
        return getArchitect().getService(Architecture.SHOW_SERVICE).getController();
    }

    @OnClick(R.id.home_go_home_button)
    void goHomeClick() {
        getShowController().show(new HomeScreen("Next home " + ++count));
    }

    @OnClick(R.id.home_go_home_custom_transition_button)
    void goHomeCustomTransitionClick() {
        getShowController().show(new HomeScreen("Next home " + ++count), Architecture.SHOW_SERVICE_TOP_TRANSITION);
    }

    @OnClick(R.id.home_go_home2_button)
    void goHome2Click() {
        getShowController().show(new HomeScreen2());
    }

    @OnClick(R.id.home_hide_result_button)
    void hideWithResultClick() {
        getShowController().hide("This is a result");
    }

    @OnClick(R.id.home_hideall_button)
    void hideAllClick() {
        getShowController().hideAll();
    }
}
