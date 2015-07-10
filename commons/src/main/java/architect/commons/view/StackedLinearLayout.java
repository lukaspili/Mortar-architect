package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;

import architect.StackFactory;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class StackedLinearLayout<T extends ViewPresenter> extends PresentedLinearLayout<T>
        implements StackedContainer {

    public StackedLinearLayout(Context context) {
        super(context);
    }

    public StackedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StackedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
