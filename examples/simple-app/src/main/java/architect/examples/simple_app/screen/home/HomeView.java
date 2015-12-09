package architect.examples.simple_app.screen.home;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import architect.examples.simple_app.Architector;
import architect.examples.simple_app.Architecture;
import architect.examples.simple_app.R;
import architect.examples.simple_app.screen.popup1.Popup1Screen;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeView extends LinearLayout {

    private static int count = 0;

    @Bind(R.id.home_toolbar)
    public Toolbar toolbar;

    public HomeView(Context context, String name, String result) {
        super(context);

        View view = View.inflate(context, R.layout.screen_home, this);
        ButterKnife.bind(view);

        if (result != null) {
            Log.d(getClass().getName(), "Got result: " + result);
        }

        toolbar.setTitle(name);
    }

    @OnClick(R.id.home_new_home)
    void homeClick() {
        Architector.getNavigationController(this).push(new HomeScreen("New home " + ++count));
    }

    @OnClick(R.id.home_new_home_with_custom_transition)
    void homeWithCustomTransitionClick() {
        Architector.getNavigationController(this).push(new HomeScreen("New home " + ++count), Architecture.NAVIGATION_SERVICE_LATERAL_TRANSITION);
    }

    @OnClick(R.id.home_new_popup1)
    void popup1Click() {
        Architector.getShowController(this).show(new Popup1Screen("New popup from home"));
    }

    @OnClick(R.id.home_back_with_result)
    void backWithResultClick() {
        Architector.getNavigationController(this).pop("This a result from home");
    }

    @OnClick(R.id.home_back_root)
    void backToRootClick() {
        Architector.getNavigationController(this).popToRoot();
    }
}
