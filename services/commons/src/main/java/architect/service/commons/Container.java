package architect.service.commons;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Container extends HandlesBack {

    void willBeginTransition();

    void didEndTransition();
}