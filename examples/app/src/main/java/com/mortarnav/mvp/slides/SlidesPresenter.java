package architect.examples.mortar_app.mvp.slides;

import android.os.Bundle;

import com.mortarnav.StandardAutoComponent;
import architect.examples.mortar_app.deps.RestClient;
import architect.examples.mortar_app.mvp.banner.BannerPresenter;
import architect.examples.mortar_app.mvp.slides.page.screen.SlidePageScreen;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Random;

import architect.robot.AutoScreen;
import architect.robot.ContainsSubscreen;
import architect.robot.NavigationParam;
import architect.robot.NavigationResult;
import architect.robot.ScreenData;
import autodagger.AutoComponent;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoScreen(
        component = @AutoComponent(includes = StandardAutoComponent.class),
        pathView = SlidesView.class,
        subScreens = {
                @ContainsSubscreen(type = BannerPresenter.class, name = "banner")
        }
)
public class SlidesPresenter extends ViewPresenter<SlidesView> {

    private final RestClient restClient;

    @ScreenData
    private final State mState;

    @NavigationParam
    private final String mParam1;

    @NavigationParam(group = {0, 1})
    private final String param2;

    @NavigationParam(group = 1)
    private final int yo;

    @NavigationResult
    private final String result;

    public SlidesPresenter(RestClient restClient, State state, String mParam1, String param2, int yo, String result) {
        this.restClient = restClient;
        this.mState = state;
        this.mParam1 = mParam1;
        this.param2 = param2;
        this.yo = yo;
        this.result = result;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        Timber.d("SlidesPresenter onLoad() with random = %d", mState.random);

        getView().show(mState.pages);
    }

    @Parcel(parcelsIndex = false)
    public static class State {

        int random;
        SlidePageScreen[] pages;

        public State() {
            random = new Random().nextInt(100);
            pages = new SlidePageScreen[]{new SlidePageScreen("page 1"), new SlidePageScreen("page 2"), new SlidePageScreen("page 3")};
        }

        @ParcelConstructor
        public State(int random, SlidePageScreen[] pages) {
            this.random = random;
            this.pages = pages;
        }
    }
}
