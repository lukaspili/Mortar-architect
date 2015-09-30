package architect;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Navigation {

    public Navigation push(ScreenPath screen) {
        return this;
    }

    public Navigation push(Navigator.PathBuilder builder) {
        return this;
    }

    public Navigation back(Object result) {
        return this;
    }

    public static Type forward(ScreenPath screen, ViewTransition transition) {
        return new Type();
    }

    public static Type forward(ScreenPath screen) {
        return forward(screen, null);
    }

    public static Type backward(ScreenPath screen, Object result, ViewTransition transition) {
        return new Type();
    }

    public static Type backward(ScreenPath screen, ViewTransition transition) {
        return backward(screen, null, transition);
    }

    public static Type backward(ScreenPath screen, Object result) {
        return backward(screen, result, null);
    }

    public static Type backward(ScreenPath screen) {
        return backward(screen, null, null);
    }

    public static Type replace(ScreenPath screen, ViewTransition transition) {
        return new Type();
    }

    public static Type replace(ScreenPath screen) {
        return replace(screen, null);
    }

    public static Type modal(ScreenPath screen, ViewTransition transition) {
        return new Type();
    }

    public static Type modal(ScreenPath screen) {
        return modal(screen, null);
    }

    public Navigation perform(Type type) {
        return this;
    }

    public static class Type {

    }


}
