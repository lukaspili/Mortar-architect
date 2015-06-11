package mortarnav.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import mortarnav.library.view.HandlesBack;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorContainerView extends FrameLayout implements HandlesBack {

    private TransitionManager transitionManager;
    private boolean interactionsDisabled;

    public NavigatorContainerView(Context context) {
        super(context);
    }

    public NavigatorContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !interactionsDisabled && super.dispatchTouchEvent(ev);
    }

    public boolean hasCurrentView() {
        return getChildCount() > 0;
    }

    /**
     * Current view is the last one
     */
    public View getCurrentView() {
        return hasCurrentView() ? getChildAt(getChildCount() - 1) : null;
    }

    public void performTransition(final View newView, final Dispatcher.Direction direction, final Dispatcher.TraversalCallback callback) {
        Preconditions.checkArgument(!interactionsDisabled, "Perform transition but previous one is still running");
        Preconditions.checkNotNull(transitionManager, "Cannot perform transition without transitionner set");
        interactionsDisabled = true;

        final View currentView = getCurrentView();

        if (currentView == null) {
            // no previous view, add and show directly
            addView(newView);
            endTransition(callback);
            return;
        }

        // add view at the end, or before the current one if backward direction
        if (direction == Dispatcher.Direction.FORWARD) {
            addView(newView);
        } else {
            addView(newView, getChildCount() - 1);
        }

        Util.waitForMeasure(newView, new Util.OnMeasuredCallback() {
            @Override
            public void onMeasured(View view, int width, int height) {
                transitionManager.transition(currentView, view, direction, new Dispatcher.TraversalCallback() {
                    @Override
                    public void onTraversalCompleted() {
                        removeView(currentView);
                        endTransition(callback);
                    }
                });
            }
        });
    }

    private void endTransition(Dispatcher.TraversalCallback callback) {
        Preconditions.checkArgument(interactionsDisabled, "End transition but perform transition did not start");

        interactionsDisabled = false;
        callback.onTraversalCompleted();
    }


    // HandlesBack

    @Override
    public boolean onBackPressed() {
        if (interactionsDisabled) {
            return true;
        }

        if (hasCurrentView() && getCurrentView() instanceof HandlesBack) {
            return ((HandlesBack) getCurrentView()).onBackPressed();
        }

        return false;
    }

    public void setTransitionManager(TransitionManager transitionManager) {
        this.transitionManager = transitionManager;
    }

    protected static class Util {

        /**
         * Copy paste from Square Flow
         */
        public static void waitForMeasure(final View view, final OnMeasuredCallback callback) {
            int width = view.getWidth();
            int height = view.getHeight();

            if (width > 0 && height > 0) {
                callback.onMeasured(view, width, height);
                return;
            }

            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    final ViewTreeObserver observer = view.getViewTreeObserver();
                    if (observer.isAlive()) {
                        observer.removeOnPreDrawListener(this);
                    }

                    callback.onMeasured(view, view.getWidth(), view.getHeight());

                    return true;
                }
            });
        }

        private Util() {
        }

        public interface OnMeasuredCallback {
            void onMeasured(View view, int width, int height);
        }
    }
}
