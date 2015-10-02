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
//public class BottomAppearTransition extends ModalTransition {
//
//    protected boolean hideExitView = true;
//
//    public BottomAppearTransition() {
//    }
//
//    public BottomAppearTransition(Config config) {
//        super(config);
//    }
//
//    /**
//     * @param hideExitView should the transition hide the exit view?
//     */
//    public BottomAppearTransition(boolean hideExitView) {
//        this.hideExitView = hideExitView;
//    }
//
//    /**
//     * @param hideExitView should the transition hide the exit view?
//     */
//    public BottomAppearTransition(boolean hideExitView, Config config) {
//        super(config);
//        this.hideExitView = hideExitView;
//    }
//
//    @Override
//    public void performTransition(View enterView, View exitView, ViewTransitionDirection direction, AnimatorSet set) {
//        super.performTransition(enterView, exitView, direction, set);
//
//        if (direction == ViewTransitionDirection.FORWARD) {
//            set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_Y, enterView.getHeight(), 0));
//        } else {
//            set.play(ObjectAnimator.ofFloat(exitView, View.TRANSLATION_Y, 0, exitView.getHeight()));
//        }
//    }
//
//    @Override
//    public boolean hideExitView() {
//        return hideExitView;
//    }
//}
