package architect;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Controller {

    void show(History.Entry entry, DispatchEnv env, Callback callback);

    interface Callback {
        void onComplete();
    }
}
