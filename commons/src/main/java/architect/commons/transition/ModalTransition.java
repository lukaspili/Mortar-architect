//package architect.commons.transition;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.view.View;
//import android.view.animation.AccelerateDecelerateInterpolator;
//
//import architect.ViewTransition;
//import architect.ViewTransitionDirection;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public abstract class ModalTransition<T extends View> implements ViewTransition<T, View> {
//
//    protected Config config;
//
//    public ModalTransition() {
//        this(new Config().duration(300).interpolator(new AccelerateDecelerateInterpolator()));
//    }
//
//    public ModalTransition(Config config) {
//        this.config = config;
//    }
//
//    @Override
//    public void performTransition(final T enterView, final View exitView, ViewTransitionDirection direction, AnimatorSet set) {
//        if (hideExitView()) {
//            if (direction == ViewTransitionDirection.FORWARD) {
//                set.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        exitView.setVisibility(View.GONE);
//                    }
//                });
//            } else {
//                set.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        enterView.setVisibility(View.VISIBLE);
//                    }
//                });
//            }
//        }
//    }
//
//    public abstract boolean hideExitView();
//}
