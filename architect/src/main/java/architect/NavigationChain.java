package architect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigationChain {

    List<Chain> chains = new ArrayList<>();
    Object result;

    public NavigationChain back() {
        chains.add(new Chain(Chain.TYPE_BACK));
        return this;
    }

    public NavigationChain back(Object result) {
        chains.add(new Chain(Chain.TYPE_BACK));
        this.result = result;
        return this;
    }

    public NavigationChain backToRoot() {
        chains.add(new Chain(Chain.TYPE_BACK_ROOT));
        return this;
    }

    public NavigationChain backToRoot(Object result) {
        chains.add(new Chain(Chain.TYPE_BACK_ROOT));
        this.result = result;
        return this;
    }

    public NavigationChain push(StackablePath path) {
        chains.add(new Chain(path, Chain.TYPE_PUSH));
        return this;
    }

    public NavigationChain show(StackablePath path) {
        chains.add(new Chain(path, Chain.TYPE_SHOW));
        return this;
    }

    public NavigationChain replace(StackablePath path) {
        chains.add(new Chain(path, Chain.TYPE_REPLACE));
        return this;
    }

    public static class Chain {

        static final int TYPE_PUSH = 1;
        static final int TYPE_SHOW = 2;
        static final int TYPE_REPLACE = 3;
        static final int TYPE_BACK = 4;
        static final int TYPE_BACK_ROOT = 5;

        StackablePath path;
        int type;

        public Chain(int type) {
            this(null, type);
        }

        public Chain(StackablePath path, int type) {
            this.path = path;
            this.type = type;
        }
    }
}
