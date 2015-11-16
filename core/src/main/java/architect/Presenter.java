package architect;

import android.view.ViewGroup;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class Presenter {

    protected final ViewGroup containerView;

    public Presenter(ViewGroup containerView) {
        this.containerView = containerView;
    }

//    public final void takeView(ViewGroup view) {
//        containerView = view;
//    }
//
//    public final void dropView() {
////        Preconditions.checkArgument(containerView == view, "");
//        containerView = null;
//    }
//
//    protected ViewGroup getView() {
//        return containerView;
//    }

    public abstract void present(History.Entry enterEntry, History.Entry exitEntry, boolean forward, DispatchEnv env, Callback callback);

    public abstract boolean onBackPressed();


}
