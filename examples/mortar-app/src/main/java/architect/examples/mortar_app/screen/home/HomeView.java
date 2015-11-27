package architect.examples.mortar_app.screen.home;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import architect.Architect;
import architect.examples.mortar_app.Architecture;
import architect.examples.mortar_app.MainActivity;
import architect.examples.mortar_app.R;
import architect.hook.mortar.MortarAchitect;
import architect.service.show.ShowController;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeView extends LinearLayout {

    private static int count = 0;

    private final HomePresenter presenter;

    @Bind(R.id.home_title)
    public TextView titleTextView;

    @Bind(R.id.home_subtitle)
    public TextView subtitleTextView;

    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    public HomeView(Context context, String name) {
        super(context);

        presenter = MortarScope.getScope(context).getService("presenter");

        View view = View.inflate(context, R.layout.home_view, this);
        ButterKnife.bind(view);

        toolbar.setTitle("Home: " + name);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }

    private ShowController getShowController() {
        return MortarAchitect.get(this).getService(Architecture.SHOW_SERVICE).getController();
    }

    @OnClick(R.id.next_home_button)
    void nextHomeClick() {
        getShowController().show(new HomeScreen("Next home " + ++count));
    }

    @OnClick(R.id.show_popup)
    void showPopupClick() {
        getShowController().show(new HomeScreen("Next home " + ++count), "top");
    }

    @OnClick(R.id.replace_new_home)
    void replaceNewHomeClick() {
    }

    @OnClick(R.id.show_returns_result)
    void showReturnsResultClick() {
    }

    @OnClick(R.id.back_root)
    void backRootClick() {
    }

    @OnClick(R.id.home_set_new_stack)
    void setNewStackClick() {
    }


}
