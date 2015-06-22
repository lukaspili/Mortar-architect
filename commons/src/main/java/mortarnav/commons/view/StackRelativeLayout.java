package mortarnav.commons.view;

import android.content.Context;
import android.util.AttributeSet;

import mortar.ViewPresenter;
import mortarnav.StackScope;
import mortarnav.StackFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class StackRelativeLayout<T extends ViewPresenter> extends MvpRelativeLayout<T> {

    public StackRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public StackRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StackRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
