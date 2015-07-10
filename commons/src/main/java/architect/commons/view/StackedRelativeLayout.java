package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;

import architect.StackFactory;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class StackedRelativeLayout<T extends ViewPresenter> extends PresentedRelativeLayout<T>
        implements StackedContainer {

    public StackedRelativeLayout(Context context) {
        super(context);
    }

    public StackedRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StackedRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
