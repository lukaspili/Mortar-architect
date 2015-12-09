package architect.examples.simple_app.screen.popup1;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import architect.examples.simple_app.Architector;
import architect.examples.simple_app.Architecture;
import architect.examples.simple_app.R;
import architect.examples.simple_app.screen.popup2.Popup2Screen;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lukasz on 09/12/15.
 */
public class Popup1View extends LinearLayout {

    @Bind(R.id.popup1_title)
    TextView popup1TextView;

    public Popup1View(Context context, String title) {
        super(context);

        View view = View.inflate(context, R.layout.screen_popup1, this);
        ButterKnife.bind(view);

        popup1TextView.setText(title);
    }

    @OnClick(R.id.popup1_new_popup)
    void newPopup1Click() {
        Architector.getShowController(this).show(new Popup1Screen("New popup from popup"));
    }

    @OnClick(R.id.popup1_new_popup_custom_transition)
    void newPopup1CustomTransitionClick() {
        Architector.getShowController(this).show(new Popup1Screen("New popup from popup"), Architecture.SHOW_SERVICE_TOP_TRANSITION);
    }

    @OnClick(R.id.popup1_new_popup2)
    void newPopup2Click() {
        Architector.getShowController(this).show(new Popup2Screen("New popup 2 from popup 1"));
    }

    @OnClick(R.id.popup1_hide_with_result)
    void hideWithResultClick() {
        Architector.getShowController(this).hide("This is a result");
    }

    @OnClick(R.id.popup1_hide_all)
    void hideAllClick() {
        Architector.getShowController(this).hideAll();
    }
}
