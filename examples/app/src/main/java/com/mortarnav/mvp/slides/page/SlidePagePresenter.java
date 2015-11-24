package architect.examples.mortar_app.mvp.slides.page;

import android.os.Bundle;

import architect.examples.mortar_app.deps.WithActivityDependencies;
import architect.examples.mortar_app.mvp.home.HomeScreen;
import architect.examples.mortar_app.mvp.slides.SlidesPresenter;

import architect.Architect;
import architect.robot.AutoScreen;
import architect.robot.NavigationParam;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoScreen(
        component = @AutoComponent(dependencies = SlidesPresenter.class, superinterfaces = WithActivityDependencies.class),
        pathView = SlidePageView.class
)
public class SlidePagePresenter extends ViewPresenter<SlidePageView> {

    @NavigationParam
    private final String name;

    public SlidePagePresenter(String name) {
        this.name = name;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        getView().configure(name);
    }

    public void buttonClick() {
        Architect.get(getView()).push(new HomeScreen("New Home from SlidePage"));
    }
}
