package architect;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigationChain {

    List<Chain> chains = new LinkedList<>();

    public NavigationChain back() {
        chains.add(new Chain(null));
        return this;
    }

    public NavigationChain push(StackPath path) {
        chains.add(new Chain(path));
        return this;
    }

    public static class Chain {
        StackPath path;

        Chain(StackPath path) {
            this.path = path;
        }
    }
}
