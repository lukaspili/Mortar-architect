package mortarnav.library;

import android.content.Context;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Navigator {

    public static final String SERVICE_NAME = Navigator.class.getName();

    public static Navigator get(Context context) {
        return MortarScope.getScope(context).getService(SERVICE_NAME);
    }

    private final NavigatorLifecycleDelegate delegate;
    private final Dispatcher dispatcher;

    public Navigator() {
        delegate = new NavigatorLifecycleDelegate(this);
        dispatcher = new Dispatcher();
    }

    public void set(Screen screen) {

    }

    public NavigatorLifecycleDelegate delegate() {
        return delegate;
    }
}
