package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;

import architect.StackFactory;
import architect.StackScope;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class StackedFrameLayout<T extends ViewPresenter> extends PresentedFrameLayout<T> {

    public StackedFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public StackedFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StackedFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        Context newContext = StackFactory.createContext(context, getScope());
        initWithContext(newContext);
    }

    public abstract StackScope getScope();

    public abstract void initWithContext(Context context);
}
