package mortarnav.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorContainerView extends FrameLayout {

    private Dispatcher dispatcher;
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

    public void makeTransition(Dispatcher.PendingNavigation pendingNavigation) {
        Preconditions.checkNotNull(dispatcher, "Dispatcher null");

        if (getChildCount() > 0) {
            removeViewAt(0);
        }

        Screen screen = pendingNavigation.getDestination();

        MortarScope scope = MortarScope.findChild(getContext(), screen.getScopeName());

        if (scope == null) {
            MortarScope.Builder builder = MortarScope.buildChild(getContext());
            screen.configureScope(MortarScope.getScope(getContext()), builder);
            scope = builder.build(screen.getClass().getName());
        }

        Context context = scope.createContext(getContext());
        addView(screen.createView(context));

        dispatcher.onTransitionEnd();
    }

    public void setDispatcher(Dispatcher dispatcher) {
        Preconditions.checkNull(this.dispatcher, "Dispatcher not null");
        Preconditions.checkNotNull(dispatcher, "Provided dispatcher null");
        this.dispatcher = dispatcher;
    }
}
