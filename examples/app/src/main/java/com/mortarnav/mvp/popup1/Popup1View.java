package architect.examples.mortar_app.mvp.popup1;

import android.content.Context;
import android.view.View;

import com.mortarnav.R;
import architect.examples.mortar_app.mvp.popup1.screen.Popup1ScreenComponent;

import architect.commons.view.PresentedFrameLayout;
import architect.robot.dagger.DaggerService;
import autodagger.AutoInjector;
import butterknife.ButterKnife;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(Popup1Presenter.class)
public class Popup1View extends PresentedFrameLayout<Popup1Presenter> {

    public Popup1View(Context context) {
        super(context);

        DaggerService.<Popup1ScreenComponent>get(context).inject(this);
        View view = View.inflate(context, R.layout.popup1_view, this);
        ButterKnife.bind(view);
    }
}
