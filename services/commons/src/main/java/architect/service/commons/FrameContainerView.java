package architect.service.commons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by lukasz on 23/11/15.
 */
public class FrameContainerView extends FrameLayout implements Container<FrameLayout> {

    protected boolean interactionsDisabled = false;

    public FrameContainerView(Context context) {
        super(context);
    }

    public FrameContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public FrameLayout getView() {
        return this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !interactionsDisabled && super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onBackPressed() {
        return interactionsDisabled;
    }

    @Override
    public void willBeginTransition() {
        interactionsDisabled = true;
    }

    @Override
    public void didEndTransition() {
        interactionsDisabled = false;
    }
}
