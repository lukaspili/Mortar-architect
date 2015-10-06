package architect.nav;

import android.animation.AnimatorSet;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface HandlesViewTransition {

    /**
     * Will receive the following view transition
     *
     * Hook that allows to add additional animations to the view transition animator set
     * Do not start the animator set yourself
     *
     * The animator set can be null, if the view is presented without animation
     * It happens at least during config changes (rotation)
     */
    void onViewTransition(AnimatorSet set);
}
