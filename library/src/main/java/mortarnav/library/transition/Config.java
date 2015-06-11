package mortarnav.library.transition;

import android.animation.AnimatorSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Config {

    private static final int DURATION = 300;

    private int duration = DURATION;
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();

    public Config duration(int duration) {
        this.duration = duration;
        return this;
    }

    public Config interpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    public void configure(AnimatorSet set) {
        set.setInterpolator(interpolator);
        set.setDuration(duration);
    }
}
