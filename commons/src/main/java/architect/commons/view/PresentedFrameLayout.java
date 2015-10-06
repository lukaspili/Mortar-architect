package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import javax.inject.Inject;

import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class PresentedFrameLayout<T extends ViewPresenter> extends FrameLayout {

    @Inject
    protected T presenter;

    public PresentedFrameLayout(Context context) {
        super(context);
        init(context, null, -1);
    }

    public PresentedFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public PresentedFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!isInEditMode()) {
            //noinspection unchecked
            presenter.takeView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (!isInEditMode()) {
            //noinspection unchecked
            presenter.dropView(this);
        }

        super.onDetachedFromWindow();
    }
}
