package architect;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Destination {

    final ScreenPath screen;
    final String transition;

    public Destination(ScreenPath screen) {
        this(screen, null);
    }

    public Destination(ScreenPath screen, String transition) {
        this.screen = screen;
        this.transition = transition;
    }
}
