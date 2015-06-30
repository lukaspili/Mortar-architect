package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class PresentedRelativeLayout<T extends ViewPresenter> extends RelativeLayout {

    @Inject
    protected T presenter;

    public PresentedRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public PresentedRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PresentedRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
