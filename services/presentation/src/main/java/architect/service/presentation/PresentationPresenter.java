package architect.service.presentation;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.List;

import architect.Callback;
import architect.DispatchEnv;
import architect.History;
import architect.Presenter;
import architect.service.commons.Container;
import architect.service.commons.HandlesBack;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PresentationPresenter extends Presenter {

    private final Transitions transitions = new Transitions();

    public PresentationPresenter(ViewGroup containerView) {
        super(containerView);
    }

    public void restore(List<History.Entry> entries) {
//        for (int i = 0; i < ; i++) {
//
//        }
    }

    @Override
    public void present(History.Entry enterEntry, History.Entry exitEntry, boolean forward, DispatchEnv env, final Callback callback) {
        final Container container = getAsContainer();
        container.willBeginTransition();

        final Callback presentationCallback = new Callback() {
            @Override
            public void onComplete() {
                container.didEndTransition();
                callback.onComplete();
            }
        };

        if (forward) {
            show(enterEntry, presentationCallback);
        } else {
            hide(exitEntry, presentationCallback);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (getAsContainer().onBackPressed()) {
            return true;
        }

        View view = containerView.getChildAt(containerView.getChildCount() - 1);
        return view instanceof HandlesBack && ((HandlesBack) view).onBackPressed();
    }

    private void show(History.Entry entry, Callback callback) {
        View newView = entry.screen.createView(containerView.getContext(), containerView);
        containerView.addView(newView);
        measureAndShow(newView, getTransition(entry), callback);
    }

    private void hide(History.Entry exitEntry, final Callback callback) {
        Transition transition = getTransition(exitEntry);
        if (transition == null) {
            containerView.removeViewAt(containerView.getChildCount() - 1);
            callback.onComplete();
            return;
        }

        transition.hide(containerView.getChildAt(containerView.getChildCount() - 1), new Callback() {
            @Override
            public void onComplete() {
                containerView.removeViewAt(containerView.getChildCount() - 1);
                callback.onComplete();
            }
        });
    }

    private Transition getTransition(History.Entry entry) {
        String key = entry.extras != null ? entry.extras.getString(EntryExtras.TRANSITION) : null;
        return transitions.find(key);
    }

    private void measureAndShow(final View view, final Transition transition, final Callback callback) {
        int width = view.getWidth();
        int height = view.getHeight();

        if (width > 0 && height > 0) {
            onShowReady(view, transition, callback);
            return;
        }

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }

                onShowReady(view, transition, callback);
                return true;
            }
        });
    }

    private void onShowReady(View view, Transition transition, Callback callback) {
        if (transition == null) {
            callback.onComplete();
            return;
        }

        transition.show(view, callback);
    }

    public Transitions transitions() {
        return transitions;
    }

    private Container getAsContainer() {
        try {
            return (Container) containerView;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Presentation container view must implement PresentationContainer");
        }
    }
}
