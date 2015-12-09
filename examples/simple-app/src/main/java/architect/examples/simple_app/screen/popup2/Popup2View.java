package architect.examples.simple_app.screen.popup2;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import architect.examples.simple_app.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lukasz on 09/12/15.
 */
public class Popup2View extends LinearLayout {

    @Bind(R.id.popup2_title)
    TextView popup2TextView;

    public Popup2View(Context context, String title) {
        super(context);

        View view = View.inflate(context, R.layout.screen_popup2, this);
        ButterKnife.bind(view);

        popup2TextView.setText(title);
    }
}
