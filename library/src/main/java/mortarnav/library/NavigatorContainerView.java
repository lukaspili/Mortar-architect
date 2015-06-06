package mortarnav.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorContainerView extends FrameLayout {

    private boolean interactionsDisabled;

    public NavigatorContainerView(Context context) {
        super(context);
    }

    public NavigatorContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !interactionsDisabled && super.dispatchTouchEvent(ev);
    }

    public boolean hasCurrentView() {
        return getChildCount() > 0;
    }

    public View getCurrentView() {
        return hasCurrentView() ? getChildAt(0) : null;
    }

    public void transitionView(View newView, Callback callback) {
        interactionsDisabled = true;

        addView(newView);

        // transition
        // todo

        if (getChildCount() > 1) {
            // remove old view if exists
            removeViewAt(0);
        }

        interactionsDisabled = false;
        callback.onTransitionEnd();
    }

    public interface Callback {
        void onTransitionEnd();
    }
}
