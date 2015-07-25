package architect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigationChain {

    List<Chain> chains = new ArrayList<>();

    public NavigationChain back() {
        chains.add(new Chain(Chain.TYPE_BACK));
        return this;
    }

    public NavigationChain backToRoot() {
        chains.add(new Chain(Chain.TYPE_BACK_ROOT));
        return this;
    }

    public NavigationChain push(ScreenPath path) {
        chains.add(new Chain(path, Chain.TYPE_PUSH));
        return this;
    }

    public NavigationChain show(ScreenPath path) {
        chains.add(new Chain(path, Chain.TYPE_SHOW));
        return this;
    }

    public NavigationChain replace(ScreenPath path) {
        chains.add(new Chain(path, Chain.TYPE_REPLACE));
        return this;
    }

    public static class Chain {

        static final int TYPE_PUSH = 1;
        static final int TYPE_SHOW = 2;
        static final int TYPE_REPLACE = 3;
        static final int TYPE_BACK = 4;
        static final int TYPE_BACK_ROOT = 5;

        ScreenPath path;
        int type;

        public Chain(int type) {
            this(null, type);
        }

        public Chain(ScreenPath path, int type) {
            this.path = path;
            this.type = type;
        }
    }
}
