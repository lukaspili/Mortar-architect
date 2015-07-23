package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import javax.inject.Inject;

import architect.view.HasPresenter;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class PresentedLinearLayout<T extends ViewPresenter> extends LinearLayout
        implements HasPresenter<T> {

    @Inject
    protected T presenter;

    public PresentedLinearLayout(Context context) {
        super(context);
        init(context, null, -1);
    }

    public PresentedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public PresentedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            presenter.takeView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (!isInEditMode()) {
            presenter.dropView(this);
        }
        super.onDetachedFromWindow();
    }

    @Override
    public T getPresenter() {
        return presenter;
    }
}
