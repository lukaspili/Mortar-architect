package architect;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PathBuilder {

    ScreenPath path;
    String transition;
    String id;

    public PathBuilder(ScreenPath path) {
        this.path = path;
    }

    public PathBuilder transition(String transition) {
        this.transition = transition;
        return this;
    }

    public PathBuilder id(String id) {
        this.id = id;
        return this;
    }
}
