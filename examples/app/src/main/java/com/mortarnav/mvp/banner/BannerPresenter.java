package architect.examples.mortar_app.mvp.banner;

import android.os.Bundle;

import com.mortarnav.MainActivity;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Random;

import architect.robot.AutoScreen;
import architect.robot.ScreenData;
import autodagger.AutoComponent;
import mortar.ViewPresenter;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoScreen(
        component = @AutoComponent(dependencies = MainActivity.class)
)
public class BannerPresenter extends ViewPresenter<BannerView> {

    @ScreenData
    private final BannerState state;

    public BannerPresenter(BannerState state) {
        this.state = state;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        Timber.d("Banner onLoad with random = %d", state.random);
    }

    public void click() {
        Timber.d("Clicked on banner!");
    }

    @Parcel(parcelsIndex = false)
    public static class BannerState {

        int random;

        public BannerState() {
            this.random = new Random().nextInt(100);
        }

        @ParcelConstructor
        public BannerState(int random) {
            this.random = random;
        }
    }
}
