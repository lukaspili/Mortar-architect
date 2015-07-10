package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;

import architect.StackFactory;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class StackedScrollView<T extends ViewPresenter> extends PresentedScrollView<T>
        implements StackedContainer {

    public StackedScrollView(Context context) {
        super(context);
    }

    public StackedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StackedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        Context newContext = StackFactory.createContext(context, getStackable(), getStackableIdentifier());
        initWithContext(newContext);
    }

    @Override
    public String getStackableIdentifier() {
        return null;
    }
}
