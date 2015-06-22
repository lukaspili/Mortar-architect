package architect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import architect.view.HandlesBack;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorView extends FrameLayout implements HandlesBack {

    private boolean interactionsDisabled;
    private View viewToRemove;

    public NavigatorView(Context context) {
        super(context);
    }

    public NavigatorView(Context context, AttributeSet attrs) {
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

    void endPresentation() {
        Preconditions.checkArgument(interactionsDisabled, "End presentation but looks like presentation never started");
        interactionsDisabled = false;

        if (viewToRemove != null) {
            removeView(viewToRemove);
            viewToRemove = null;
        }
    }

    void startPresentation(final View newView, final Dispatcher.Direction direction, final Presenter.PresenterSession session, final Presenter.PresentationCallback callback) {
        Preconditions.checkArgument(!interactionsDisabled, "Start presentation but previous one did not end");
        interactionsDisabled = true;

        final View currentView = getCurrentView();
        if (currentView == null) {
            // no previous view, add and show directly
            addView(newView);
            callback.onPresentationFinished(null, newView, session);
            return;
        }

        // add view at the end, or before the current one if backward direction
        if (direction == Dispatcher.Direction.FORWARD) {
            addView(newView);
        } else {
            addView(newView, getChildCount() - 1);
            viewToRemove = currentView;
        }

        Util.waitForMeasure(newView, new Util.OnMeasuredCallback() {
            @Override
            public void onMeasured(View view, int width, int height) {
                callback.onPresentationFinished(currentView, view, session);
            }
        });
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


    /**
     * Copy paste from Square Flow
     */
    private static class Util {

        private static void waitForMeasure(final View view, final OnMeasuredCallback callback) {
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
