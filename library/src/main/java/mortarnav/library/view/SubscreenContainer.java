package mortarnav.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mortarnav.library.NavigatorServices;
import mortarnav.library.R;
import mortarnav.library.Screen;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SubscreenContainer extends FrameLayout {

    public SubscreenContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SubscreenContainer, 0, 0);
        String cls;
        try {
            cls = a.getString(R.styleable.SubscreenContainer_screenClass);
        } finally {
            a.recycle();
        }

        if (cls == null || cls.isEmpty()) {
            throw new IllegalArgumentException("stringClass attribute cannot be null");
        }

        Screen screen;
        try {
            screen = (Screen) Class.forName(cls).newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("stringClass must reference a Screen class that has a default empty constructor", e);
        }

        Context newContext = NavigatorServices.getContextFactoryService(context).setUp(context, screen);
        addView(screen.createView(newContext), new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
