package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
        init(context);
    }

    public PresentedFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PresentedFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }
}
