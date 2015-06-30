package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;

import architect.StackScope;
import mortar.ViewPresenter;
import architect.StackFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class StackedLinearLayout<T extends ViewPresenter> extends PresenterLinearLayout<T> {

    public StackedLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public StackedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StackedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
