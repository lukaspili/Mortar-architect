package architect.examples.mortar_app.mvp.popup1;

import com.mortarnav.StandardAutoComponent;

import architect.robot.AutoScreen;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoScreen(
        component = @AutoComponent(includes = StandardAutoComponent.class),
        pathView = Popup1View.class
)
public class Popup1Presenter extends ViewPresenter<Popup1View> {
}
