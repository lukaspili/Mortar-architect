package architect.examples.mortar_app.mvp.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mortarnav.MainActivity;
import com.mortarnav.MainActivityComponent;
import architect.examples.mortar_app.deps.WithActivityDependencies;
import architect.examples.mortar_app.mvp.banner.screen.BannerScreen;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import architect.Screen;
import architect.SubScreenService;
import architect.behavior.ReceivesResult;
import architect.robot.dagger.DaggerScope;
import architect.robot.dagger.DaggerService;
import dagger.Provides;
import mortar.MortarScope;

/**
 * Manually created Screen
 * For showcase/clarity purposes only
 * You have all the reasons to use @AutoScreen and let Robot generates that code for you
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@Parcel(parcelsIndex = false)
public class HomeScreen implements Screen, ReceivesResult<String> {

    HomePresenter.HomeState state;

    BannerScreen bannerScreen;
    BannerScreen bannerScreen2;

    String name;
    String result;

    public HomeScreen(String name) {
        this.name = name;
        state = new HomePresenter.HomeState();
        bannerScreen = new BannerScreen();
        bannerScreen2 = new BannerScreen();
    }

    @ParcelConstructor
    HomeScreen(HomePresenter.HomeState state, BannerScreen bannerScreen, BannerScreen bannerScreen2, String name, String result) {
        this.state = state;
        this.bannerScreen = bannerScreen;
        this.bannerScreen2 = bannerScreen2;
        this.name = name;
        this.result = result;
    }


    @Override
    public View createView(Context context, ViewGroup parent) {
        return new HomeView(context);
    }

    @Override
    public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
        DaggerService.configureScope(builder, HomeScreen.class, DaggerHomeScreen_Component.builder()
                .mainActivityComponent(DaggerService.<MainActivityComponent>getTyped(parentScope, MainActivity.class))
                .module(new Module())
                .build());

        builder.withService(SubScreenService.SERVICE_NAME, new SubScreenService.Builder()
                .withScreen("bannerScreen", bannerScreen)
                .withScreen("bannerScreen2", bannerScreen2)
                .build());
    }

    @Override
    public void setResult(String result) {
        this.result = result;
    }

    @dagger.Module
    public class Module {

        @Provides
        @DaggerScope(Component.class)
        public HomePresenter providesPresenter() {
            return new HomePresenter(state, name, result);
        }
    }

    @dagger.Component(dependencies = MainActivityComponent.class, modules = Module.class)
    @DaggerScope(Component.class)
    public interface Component extends WithActivityDependencies {

        void inject(HomeView view);

        void inject(HomeAdditionalCustomView view);
    }
}
