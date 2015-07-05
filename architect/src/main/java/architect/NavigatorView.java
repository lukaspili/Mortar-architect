package architect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import architect.transition.ViewTransition;
import architect.view.HandlesBack;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigatorView extends FrameLayout implements HandlesBack {

    int sessionId;
    private boolean interactionsDisabled;

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

    void show(View newView, boolean addNewView, boolean removePreviousView, final Dispatcher.Direction direction, final ViewTransition transition, final Presenter.PresentationCallback callback) {
        Preconditions.checkArgument(!interactionsDisabled, "Start presentation but previous one did not end");
        Preconditions.checkArgument(sessionId > 0, "Cannot show while session is not valid");
        Preconditions.checkNotNull(newView, "New view cannot be null");
        interactionsDisabled = true;

        Logger.d("Show view: %s", newView.getClass());

        Logger.d("## Views before");
        for (int i = 0; i < getChildCount(); ++i) {
            Logger.d("%d = %s", i, getChildAt(i));
        }

        final View currentView = getCurrentView();

        if (currentView == null) {
            Preconditions.checkNotNull(newView, "New view cannot be null if current view is null");
            // no previous view, add and show directly
            addView(newView);
            end(callback);
            return;
        }

        if (addNewView) {
            addView(newView);
            Logger.d("Add view %s", newView.getClass());
        }

        if (transition == null) {
            if (removePreviousView) {
                removeView(currentView);
                Logger.d("Remove view %s", currentView.getClass());
            }
            end(callback);
        } else {
            measureAndTransition(newView, getChildAt(getChildCount() - (addNewView ? 2 : 1)), removePreviousView, direction, transition, callback);
        }
    }

    private void end(Presenter.PresentationCallback callback) {
        Logger.d("## Views after");
        for (int i = 0; i < getChildCount(); ++i) {
            Logger.d("%d = %s", i, getChildAt(i));
        }

        interactionsDisabled = false;
        callback.onPresentationFinished(sessionId);
    }

    private void transition(final View originView, View destinationView, final boolean removePreviousView, final Dispatcher.Direction direction, final ViewTransition transition, final Presenter.PresentationCallback callback) {
        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (removePreviousView) {
                    removeView(originView);
                    Logger.d("Remove view %s", originView.getClass());
                }
                end(callback);
            }
        });

        transition.configure(set);

        if (direction == Dispatcher.Direction.FORWARD) {
            transition.forward(destinationView, originView, set);
        } else {
            transition.backward(destinationView, originView, set);
        }

        set.start();
    }

    private void measureAndTransition(final View newView, final View previousView, final boolean removePreviousView, final Dispatcher.Direction direction, final ViewTransition transition, final Presenter.PresentationCallback callback) {
        int width = newView.getWidth();
        int height = newView.getHeight();

        if (width > 0 && height > 0) {
            transition(previousView, newView, removePreviousView, direction, transition, callback);
            return;
        }

        newView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final ViewTreeObserver observer = newView.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }

                transition(previousView, newView, removePreviousView, direction, transition, callback);
                return true;
            }
        });
    }

//    void endPresentation(boolean removePreviousView) {
//        Preconditions.checkArgument(interactionsDisabled, "End presentation but looks like presentation never started");
//        interactionsDisabled = false;
//
//        if (removePreviousView && previousView != null) {
//            Logger.d("Remove previous view %s", previousView);
//            removeView(previousView);
//        }
//        previousView = null;
//    }
//
//    void startPresentation(final View newView, final Dispatcher.Direction direction, final Presenter.PresenterSession session, final Presenter.PresentationCallback callback) {
//        Preconditions.checkArgument(!interactionsDisabled, "Start presentation but previous one did not end");
//        interactionsDisabled = true;
//
//        final View currentView = getCurrentView();
//        if (currentView == null) {
//            // no previous view, add and show directly
//            addView(newView);
//            callback.onPresentationFinished(null, newView, session);
//            return;
//        }
//
//        previousView = currentView;
//
//        // add view at the end, or before the current one if backward direction
//        if (direction == Dispatcher.Direction.FORWARD) {
//            addView(newView);
//        } else {
//            addView(newView, getChildCount() - 1);
//        }
//
//        measureAndTransition(newView, currentView);
//
//        measureAndTransition(newView, new OnMeasuredCallback() {
//            @Override
//            public void onMeasured(View view, int width, int height) {
//                callback.onPresentationFinished(currentView, view, session);
//            }
//        });
//    }


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
}
