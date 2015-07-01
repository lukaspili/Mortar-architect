package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;

import mortar.ViewPresenter;
import architect.StackScope;
import architect.StackFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class StackedRelativeLayout<T extends ViewPresenter> extends PresentedRelativeLayout<T> {

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
    protected void init(Context context) {
        super.init(context);

        Context newContext = StackFactory.createContext(context, getScope());
        initWithContext(newContext);
    }

    public abstract StackScope getScope();

    public abstract void initWithContext(Context context);
}
