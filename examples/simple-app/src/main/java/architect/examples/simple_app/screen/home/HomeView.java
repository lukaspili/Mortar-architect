package architect.examples.simple_app.screen.home;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import architect.Architect;
import architect.examples.simple_app.MainActivity;
import architect.examples.simple_app.R;
import architect.service.presentation.PresentationService;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomeView extends LinearLayout {

    private static int count = 0;

    @Bind(R.id.home_title)
    public TextView titleTextView;

    @Bind(R.id.home_subtitle)
    public TextView subtitleTextView;

    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    public HomeView(Context context, String name) {
        super(context);

        View view = View.inflate(context, R.layout.home_view, this);
        ButterKnife.bind(view);

        toolbar.setTitle("Home: " + name);
    }

    private Architect getArchitect() {
        return ((MainActivity) getContext()).getArchitect();
    }

    private PresentationService getArchitectPresentation() {
        return getArchitect().<PresentationService>getService("presentation");
    }

    @OnClick(R.id.next_home_button)
    void nextHomeClick() {
        getArchitectPresentation().show(new HomeScreen("Next home " + ++count));
    }

    @OnClick(R.id.show_popup)
    void showPopupClick() {
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
