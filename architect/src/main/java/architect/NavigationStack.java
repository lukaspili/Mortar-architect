package architect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class NavigationStack {

    List<StackablePath> paths = new ArrayList<>();

    public NavigationStack put(StackablePath path) {
        paths.add(path);
        return this;
    }

    public NavigationStack put(Collection<StackablePath> paths) {
        paths.addAll(paths);
        return this;
    }
}
