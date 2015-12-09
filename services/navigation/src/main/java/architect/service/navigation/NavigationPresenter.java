package architect.service.navigation;

import android.os.Parcelable;
import android.support.v4.util.SimpleArrayMap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.List;

import architect.Callback;
import architect.History;
import architect.Hooks;
import architect.Processing;
import architect.service.commons.AbstractPresenter;
import architect.service.commons.EntryExtras;
import architect.service.commons.FrameContainerView;
import architect.service.commons.HandlesBack;
import architect.service.commons.Transitions;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigationPresenter extends AbstractPresenter<FrameContainerView, NavigationTransition> {

    public NavigationPresenter(Hooks hooks, Transitions<NavigationTransition> transitions) {
        this(hooks, transitions, new SimpleArrayMap<History.Entry, Integer>());
    }

    NavigationPresenter(Hooks hooks, Transitions<NavigationTransition> transitions, SimpleArrayMap<History.Entry, Integer> entriesToViewIndexes) {
        super(hooks, transitions);
    }

    @Override
    public void restore(final List<History.Entry> entries, Processing processing) {
        Preconditions.checkArgument(container.getChildCount() == 0, "Already some children while restoring");

        History.Entry entry = entries.get(entries.size() - 1);
        container.addView(entry.screen.createView(getContext(container, entry, processing), container));
    }

    @Override
    public void present(final History.Entry enterEntry, final History.Entry exitEntry, final boolean forward, final Processing processing, final Callback callback) {
        container.willBeginTransition();

        initPresentationCallback(new Callback() {
            @Override
            public void onComplete() {
                container.removeViewAt(forward ? 0 : 1);
                container.didEndTransition();
                callback.onComplete();
            }
        });

        View exitView = container.getChildAt(0);
        View enterView = enterEntry.screen.createView(getContext(container, enterEntry, processing), container);

        if (forward) {
            SparseArray<Parcelable> viewState = new SparseArray<>();
            exitView.saveHierarchyState(viewState);
            exitEntry.viewState = viewState;
        } else {
            enterView.restoreHierarchyState(enterEntry.viewState);
        }

        container.addView(enterView, forward ? 1 : 0);
        measureAndTransition(enterView, exitView, forward, getTransition(forward ? enterEntry : exitEntry));
    }

    @Override
    public boolean onBackPressed() {
        if (container.onBackPressed()) {
            return true;
        }

        View view = container.getChildAt(container.getChildCount() - 1);
        return view instanceof HandlesBack && ((HandlesBack) view).onBackPressed();
    }

    private NavigationTransition getTransition(History.Entry entry) {
        return transitions.find(EntryExtras.from(entry).transition);
    }

    private void measureAndTransition(final View enterView, final View exitView, final boolean forward, final NavigationTransition transition) {
        int width = enterView.getWidth();
        int height = enterView.getHeight();

        if (width > 0 && height > 0) {
            onTransitionReady(enterView, exitView, forward, transition);
            return;
        }

        enterView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final ViewTreeObserver observer = enterView.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }

                onTransitionReady(enterView, exitView, forward, transition);
                return true;
            }
        });
    }

    private void onTransitionReady(final View enterView, final View exitView, boolean forward, NavigationTransition transition) {
        if (transition == null) {
            completePresentationCallback();
            return;
        }

        if (forward) {
            transition.forward(enterView, exitView, new Callback() {
                @Override
                public void onComplete() {
                    completePresentationCallback();
                }
            });
        } else {
            transition.backward(enterView, exitView, new Callback() {
                @Override
                public void onComplete() {
                    completePresentationCallback();
                }
            });
        }
    }
}
