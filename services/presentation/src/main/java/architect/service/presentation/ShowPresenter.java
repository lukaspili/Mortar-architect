package architect.service.presentation;

import android.view.View;
import android.view.ViewTreeObserver;

import java.util.List;

import architect.Callback;
import architect.DispatchEnv;
import architect.History;
import architect.service.commons.AbstractPresenter;
import architect.service.commons.EntryExtras;
import architect.service.commons.FrameContainerView;
import architect.service.commons.HandlesBack;
import architect.service.commons.PresentationCallback;
import architect.service.commons.Transitions;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ShowPresenter extends AbstractPresenter<FrameContainerView> {

    private final Transitions<Transition> transitions;

    public ShowPresenter(Transitions<Transition> transitions) {
        this.transitions = transitions;
    }

    @Override
    public void restore(List<History.Entry> entries) {
        History.Entry entry;
        for (int i = 0; i < entries.size(); ++i) {
            entry = entries.get(i);
            container.addView(entry.screen.createView(container.getContext(), container));
        }
    }

    @Override
    public void present(History.Entry enterEntry, History.Entry exitEntry, final boolean forward, DispatchEnv env, final Callback callback) {
        container.willBeginTransition();

        final Callback presentationCallback = new PresentationCallback(sessionId) {
            @Override
            public void onComplete() {
                if (!isSessionValid(presentationSessionId)) {
                    return;
                }

                if (!forward) {
                    container.removeViewAt(container.getChildCount() - 1);
                }

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
        if (container.onBackPressed()) {
            return true;
        }

        View view = container.getChildAt(container.getChildCount() - 1);
        return view instanceof HandlesBack && ((HandlesBack) view).onBackPressed();
    }

    private void show(History.Entry entry, Callback callback) {
        View newView = entry.screen.createView(container.getContext(), container);
        container.addView(newView);
        measureAndShow(newView, getTransition(entry), callback);
    }

    private void hide(History.Entry exitEntry, final Callback callback) {
        Transition transition = getTransition(exitEntry);
        if (transition == null) {
            callback.onComplete();
            return;
        }

        transition.hide(container.getChildAt(container.getChildCount() - 1), callback);
    }

    private Transition getTransition(History.Entry entry) {
        return transitions.find(EntryExtras.from(entry).transition);
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
}
