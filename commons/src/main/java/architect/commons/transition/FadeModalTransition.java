//package architect.commons.transition;
//
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.view.View;
//
//import architect.ViewTransitionDirection;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class FadeModalTransition extends ModalTransition {
//
//    protected boolean hideExitView;
//
//    public FadeModalTransition() {
//    }
//
//    public FadeModalTransition(Config config) {
//        super(config);
//    }
//
//    /**
//     * @param hideExitView should the transition hide the exit view?
//     */
//    public FadeModalTransition(boolean hideExitView) {
//        this.hideExitView = hideExitView;
//    }
//
//    /**
//     * @param hideExitView should the transition hide the exit view?
//     */
//    public FadeModalTransition(boolean hideExitView, Config config) {
//        super(config);
//        this.hideExitView = hideExitView;
//    }
//
//    @Override
//    public void performTransition(View enterView, View exitView, ViewTransitionDirection direction, AnimatorSet set) {
//        super.performTransition(enterView, exitView, direction, set);
//
//        if (direction == ViewTransitionDirection.FORWARD) {
//            set.play(ObjectAnimator.ofFloat(enterView, View.ALPHA, 0, 1));
//        } else {
//            set.play(ObjectAnimator.ofFloat(exitView, View.ALPHA, 1, 0));
//        }
//    }
//
//    @Override
//    public boolean hideExitView() {
//        return hideExitView;
//    }
//}
