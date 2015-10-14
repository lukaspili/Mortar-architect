package com.mortarnav.mvp.slides;

import com.mortarnav.DaggerScope;
import com.mortarnav.StandardAutoComponent;
import com.mortarnav.deps.RestClient;
import com.mortarnav.mvp.banner.BannerScreen;

import org.parceler.Parcel;

import architect.robot.AutoScreen;
import architect.robot.ContainsSubscreen;
import architect.robot.NavigationParam;
import architect.robot.NavigationResult;
import architect.robot.ScreenData;
import autodagger.AutoComponent;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoScreen(
        component = @AutoComponent(includes = StandardAutoComponent.class),
        pathView = SlidesView.class,
        subScreens = {
                @ContainsSubscreen(type = BannerScreen.class, name = "banner")
        }
)
@DaggerScope(SlidesPresenter.class)
public class SlidesPresenter extends ViewPresenter {

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

    @Parcel(parcelsIndex = false)
    public static class State {

    }
}
