package architect.commons.view;

import android.content.Context;
import android.util.AttributeSet;

import architect.MortarFactory;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class ScreenLinearLayout<T extends ViewPresenter> extends PresentedLinearLayout<T>
        implements ScreenViewContainer {

    protected Context screenContext;

    public ScreenLinearLayout(Context context) {
        super(context);
    }

    public ScreenLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScreenLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        screenContext = MortarFactory.createContext(context, getScreen(), getScreenUniqueId());
        initWithScreenContext(screenContext);
    }

//    @Override
//    public Context getScreenContext() {
//        return screenContext;
//    }

    @Override
    public String getScreenUniqueId() {
        return null;
    }
}
