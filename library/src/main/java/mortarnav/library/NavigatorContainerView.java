package mortarnav.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
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


}
