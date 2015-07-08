package architect;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ReceivesResult<T> {

    /**
     * Receive result from previous view controller
     * Beware that this method is called outside of the ViewPresenter lifecycle
     * It means that it will be called before onLoad() and getView() will return null
     */
    void onReceivedResult(T result);
}
