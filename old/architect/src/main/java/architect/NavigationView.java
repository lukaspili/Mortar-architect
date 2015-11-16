package architect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import architect.nav.HandlesBack;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigationView extends FrameLayout implements HandlesBack {

    int sessionId;
    private boolean interactionsDisabled;

    private View transitionExitView;
    private boolean transitionRemoveExitView;

    public NavigationView(Context context) {
        super(context);
    }

    public NavigationView(Context context, AttributeSet attrs) {
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

    void beginTransition(View enterView, boolean forward, int sessionId, Callback2 callback) {
        Preconditions.checkArgument(!interactionsDisabled, "Start presentation but previous one did not end");
        Preconditions.checkArgument(sessionId > 0, "Cannot show while session is not valid");
        interactionsDisabled = true;

        transitionExitView = getCurrentView();
        Preconditions.checkNotNull(transitionExitView, "exitView cannot be null");

        if (enterView == null) {
            Preconditions.checkArgument(!forward, "Reuse enter view in forward only");
            enterView = getChildAt(getChildCount() - 2);
        } else {
            if (forward) {
                addView(enterView);
            } else {
                addView(enterView, getChildCount() - 1);
            }
        }

        measureAndGetTransition(enterView, transitionExitView, sessionId, callback);
    }

    void endTransition(View exitView, boolean removeExitView) {
        Preconditions.checkArgument(interactionsDisabled, "ArchitectView end but should not");

        if (removeExitView) {
            removeView(exitView);
        }

        interactionsDisabled = false;
    }

    private void measureAndGetTransition(final View enterView, final View exitView, final int sessionId, final Callback2 callback) {
        int width = enterView.getWidth();
        int height = enterView.getHeight();

        if (width > 0 && height > 0) {
            callback.onViewReady(enterView, exitView, sessionId);
            return;
        }

        enterView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final ViewTreeObserver observer = enterView.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }

                callback.onViewReady(enterView, exitView, sessionId);
                return true;
            }
        });
    }


    //    void show(final Presentation presentation, final Presenter.PresentationCallback callback) {
//        Preconditions.checkArgument(!interactionsDisabled, "Start presentation but previous one did not end");
//        Preconditions.checkArgument(sessionId > 0, "Cannot show while session is not valid");
//        interactionsDisabled = true;
//
//        Logger.d("## Views before");
//        for (int i = 0; i < getChildCount(); ++i) {
//            Logger.d("%d = %s", i, getChildAt(i));
//        }
//
//        loadTransition(presentation, 1, new Callback() {
//            @Override
//            public void onAnimatorReady(Animator animator) {
//                if (animator != null) {
//                    animator.start();
//                } else {
//                    end(callback);
//                }
//            }
//
//            @Override
//            public void onAnimatorEnd() {
//                end(callback);
//            }
//        });
//    }

//    void showModals(final List<Presentation> presentations, final Presenter.PresentationCallback callback) {
//        Preconditions.checkArgument(!interactionsDisabled, "Start presentation but previous one did not end");
//        Preconditions.checkArgument(sessionId > 0, "Cannot show while session is not valid");
//        Preconditions.checkArgument(presentations != null && !presentations.isEmpty(), "Presentations cannot be null nor empty");
//        interactionsDisabled = true;
//
//        Logger.d("## Views before");
//        for (int i = 0; i < getChildCount(); ++i) {
//            Logger.d("%d = %s", i, getChildAt(i));
//        }
//
//        final List<Animator> animators = new ArrayList<>(presentations.size());
//        Callback getCallback = new Callback() {
//            int readyCount;
//
//            @Override
//            public void onAnimatorReady(Animator animator) {
//                animators.add(animator);
//                markReady();
//            }
//
//            @Override
//            public void onAnimatorEnd() {
//
//            }
//
//            private void markReady() {
//                readyCount++;
//                Preconditions.checkArgument(readyCount <= presentations.size(), "Ready count cannot exceed presentations size");
//
//                if (readyCount == presentations.size()) {
//                    if (!animators.isEmpty()) {
//
//                        AnimatorSet set = new AnimatorSet();
//                        set.addListener(new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                end(callback);
//                            }
//                        });
//                        set.playTogether(animators);
//                        set.start();
//                    } else {
//                        end(callback);
//                    }
//                }
//            }
//        };
//
//        for (int i = 0; i < presentations.size(); i++) {
//            loadTransition(presentations.get(i), i + 1, getCallback);
//        }
//    }

//    void loadTransition(final Presentation presentation, int previousDecount, final Callback callback) {
//        Logger.d("Load transition for: %s", presentation.view.getClass());
//        final View currentView = getCurrentView();
//
//        if (currentView == null) {
//            Preconditions.checkNotNull(presentation.view, "New view cannot be null if current view is null");
//            // no previous view, add and show directly
//            addView(presentation.view);
//            sendTransitionEvents(presentation.view, null);
//            callback.onAnimatorReady(null);
//            return;
//        }
//
//        if (presentation.addView) {
//            addView(presentation.view);
//            Logger.d("Add view %s", presentation.view.getClass());
//        }
//
//        if (presentation.transition == null) {
//            if (presentation.removePreviousView) {
//                removeView(currentView);
//                Logger.d("Remove view %s", currentView.getClass());
//            }
//            sendTransitionEvents(presentation.view, null);
//            callback.onAnimatorReady(null);
//        } else {
//            measureAndGetTransition(presentation.view, getChildAt(getChildCount() - previousDecount - (presentation.addView ? 1 : 0)), presentation.removePreviousView, presentation.direction, presentation.transition, callback);
//        }
//    }

//    private void sendTransitionEvents(View destinationView, AnimatorSet set) {
//        if (destinationView instanceof HandlesViewTransition) {
//            ((HandlesViewTransition) destinationView).onViewTransition(set);
//        }
//    }
//
//    private void end(Presenter.PresentationCallback callback) {
//        Logger.d("## Views after");
//        for (int i = 0; i < getChildCount(); ++i) {
//            Logger.d("%d = %s", i, getChildAt(i));
//        }
//
//        interactionsDisabled = false;
//        callback.onPresentationFinished(sessionId);
//    }

//    private void measureAndGetTransition(final View newView, final View previousView, final boolean removePreviousView, final int direction, final ViewTransition transition, final Callback callback) {
//        int width = newView.getWidth();
//        int height = newView.getHeight();
//
//        if (width > 0 && height > 0) {
//            callback.onAnimatorReady(getAnimator(previousView, newView, removePreviousView, direction, transition, callback));
//            return;
//        }
//
//        newView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                final ViewTreeObserver observer = newView.getViewTreeObserver();
//                if (observer.isAlive()) {
//                    observer.removeOnPreDrawListener(this);
//                }
//
//                callback.onAnimatorReady(getAnimator(previousView, newView, removePreviousView, direction, transition, callback));
//                return true;
//            }
//        });
//    }

//    private Animator getAnimator(final View originView, final View destinationView, final boolean removePreviousView, final int direction, final ViewTransition transition, final Callback callback) {
//        AnimatorSet set = new AnimatorSet();
//        set.addListener(new AnimatorListenerAdapter() {
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                if (removePreviousView) {
//                    removeView(originView);
//                    Logger.d("Remove view %s", originView.getClass());
//                }
//
//                callback.onAnimatorEnd();
//            }
//        });
//
//        transition.performTransition(destinationView, originView, direction, set);
//        sendTransitionEvents(destinationView, set);
//
//        return set;
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

//    static class Presentation {
//        final View view;
//        final boolean addView;
//        final boolean removePreviousView;
//        final int direction;
//        final ViewTransition transition;
//
//        public Presentation(View view, boolean addView, boolean removePreviousView, int direction, ViewTransition transition) {
//            this.view = view;
//            this.addView = addView;
//            this.removePreviousView = removePreviousView;
//            this.direction = direction;
//            this.transition = transition;
//        }
//    }
//
//    private interface Callback {
//
//        void onAnimatorReady(Animator animator);
//
//        void onAnimatorEnd();
//    }

    interface Callback2 {
        void onViewReady(View enterView, View exitView, int sessionId);
    }
}
