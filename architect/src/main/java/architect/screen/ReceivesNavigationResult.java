package architect.screen;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ReceivesNavigationResult<T> {

    void onReceiveNavigationResult(T result);
}
