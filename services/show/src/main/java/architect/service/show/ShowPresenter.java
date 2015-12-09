package architect.service.show;

import android.support.v4.util.SimpleArrayMap;
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
public class ShowPresenter extends AbstractPresenter<FrameContainerView, ShowTransition> {

    private final SimpleArrayMap<History.Entry, Integer> entriesToViewIndexes;

    public ShowPresenter(Hooks hooks, Transitions<ShowTransition> transitions) {
        this(hooks, transitions, new SimpleArrayMap<History.Entry, Integer>());
    }

    ShowPresenter(Hooks hooks, Transitions<ShowTransition> transitions, SimpleArrayMap<History.Entry, Integer> entriesToViewIndexes) {
        super(hooks, transitions);
        this.entriesToViewIndexes = entriesToViewIndexes;
    }

    @Override
    public void dropContainer(FrameContainerView container) {
        super.dropContainer(container);
        entriesToViewIndexes.clear();
    }

    @Override
    public void restore(final List<History.Entry> entries, Processing processing) {
        Preconditions.checkArgument(container.getChildCount() == 0, "Already some children while restoring");
        Preconditions.checkArgument(entriesToViewIndexes.isEmpty(), "Entries to view indexes must be empty on restore");

        History.Entry entry;
        for (int i = 0; i < entries.size(); ++i) {
            entry = entries.get(i);
            container.addView(entry.screen.createView(getContext(container, entry, processing), container));
            entriesToViewIndexes.put(entry, i);
        }
    }

    @Override
    public void present(final History.Entry enterEntry, History.Entry exitEntry, final boolean forward, Processing processing, final Callback callback) {
        container.willBeginTransition();

        initPresentationCallback(new Callback() {
            @Override
            public void onComplete() {
                if (!forward) {
                    if (enterEntry != null) {
                        container.removeViewAt(container.getChildCount() - 1);
                    } else {
                        container.removeAllViews();
                    }
                }

                container.didEndTransition();
                callback.onComplete();
            }
        });

        if (forward) {
            show(enterEntry, processing);
        } else {
            hide(exitEntry, enterEntry == null);
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

    private void show(History.Entry entry, Processing processing) {
        View newView = entry.screen.createView(getContext(container, entry, processing), container);
        container.addView(newView);
        entriesToViewIndexes.put(entry, container.getChildCount() - 1);

        measureAndShow(newView, getTransition(entry));
    }

    private void hide(History.Entry exitEntry, boolean hideAllViews) {
        if (hideAllViews && entriesToViewIndexes.size() > 1) {
            hideAll();
            return;
        }

        entriesToViewIndexes.clear();

        ShowTransition transition = getTransition(exitEntry);
        if (transition == null) {
            completePresentationCallback();
            return;
        }

        transition.hide(container.getChildAt(container.getChildCount() - 1), new Callback() {
            @Override
            public void onComplete() {
                completePresentationCallback();
            }
        });
    }

    private void hideAll() {
        SimpleArrayMap<View, ShowTransition> viewsTransitions = new SimpleArrayMap<>(entriesToViewIndexes.size());
        ShowTransition transition;
        for (int i = entriesToViewIndexes.size() - 1; i >= 0; i--) {
            transition = getTransition(entriesToViewIndexes.keyAt(i));
            if (transition != null) {
                viewsTransitions.put(container.getChildAt(entriesToViewIndexes.valueAt(i)), transition);
            }

            entriesToViewIndexes.removeAt(i);
        }

        if (viewsTransitions.isEmpty()) {
            completePresentationCallback();
            return;
        }

        final int total = viewsTransitions.size();
        final Callback singleCallback = new Callback() {
            private int count = 0;

            @Override
            public void onComplete() {
                if (++count == total) {
                    completePresentationCallback();
                }
            }
        };

        for (int i = 0; i < viewsTransitions.size(); i++) {
            viewsTransitions.valueAt(i).hide(viewsTransitions.keyAt(i), singleCallback);
        }
    }

    private ShowTransition getTransition(History.Entry entry) {
        return transitions.find(EntryExtras.from(entry).transition);
    }

    private void measureAndShow(final View view, final ShowTransition transition) {
        int width = view.getWidth();
        int height = view.getHeight();

        if (width > 0 && height > 0) {
            onShowReady(view, transition);
            return;
        }

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }

                onShowReady(view, transition);
                return true;
            }
        });
    }

    private void onShowReady(View view, ShowTransition transition) {
        if (transition == null) {
            completePresentationCallback();
            return;
        }

        transition.show(view, new Callback() {
            @Override
            public void onComplete() {
                completePresentationCallback();
            }
        });
    }
}
