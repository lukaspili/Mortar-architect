package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;

import architect.StackScope;
import mortar.ViewPresenter;
import architect.StackFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class StackLinearLayout<T extends ViewPresenter> extends MvpLinearLayout<T> {

    public StackLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public StackLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StackLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
