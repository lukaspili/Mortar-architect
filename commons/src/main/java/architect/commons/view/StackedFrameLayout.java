package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;

import architect.StackFactory;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class StackedFrameLayout<T extends ViewPresenter> extends PresentedFrameLayout<T>
        implements StackedContainer {

    public StackedFrameLayout(Context context) {
        super(context);
    }

    public StackedFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StackedFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        Context newContext = StackFactory.createContext(context, getScope(), getScopeIdentifier());
        initWithContext(newContext);
    }

    @Override
    public String getScopeIdentifier() {
        return null;
    }
}
