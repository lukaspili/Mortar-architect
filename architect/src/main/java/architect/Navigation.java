package architect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Navigation {

    static final int TYPE_PUSH = 1;
    static final int TYPE_SHOW = 2;
    static final int TYPE_REPLACE = 3;
    static final int TYPE_BACK = 4;
    static final int TYPE_BACK_ROOT = 5;

    List<Step> steps = new ArrayList<>();

    public Navigation push(ScreenPath screen) {
        steps.add(new Step(screen, TYPE_PUSH));
        return this;
    }

    public Navigation push(PathBuilder builder) {
        steps.add(new Step(builder, TYPE_PUSH));
        return this;
    }

    public Navigation show(ScreenPath screen) {
        steps.add(new Step(screen, TYPE_SHOW));
        return this;
    }

    public Navigation show(PathBuilder builder) {
        steps.add(new Step(builder, TYPE_SHOW));
        return this;
    }

    public Navigation replace(ScreenPath screen) {
        steps.add(new Step(screen, TYPE_REPLACE));
        return this;
    }

    public Navigation replace(PathBuilder builder) {
        steps.add(new Step(builder, TYPE_REPLACE));
        return this;
    }

    public Navigation back() {
        steps.add(new Step(TYPE_BACK));
        return this;
    }

    public Navigation back(Object result) {
        steps.add(new Step(result, TYPE_BACK));
        return this;
    }

    public Navigation backToRoot() {
        steps.add(new Step(TYPE_BACK_ROOT));
        return this;
    }

    public Navigation backToRoot(Object result) {
        steps.add(new Step(result, TYPE_BACK_ROOT));
        return this;
    }

    static class Step {
        final ScreenPath path;
        final PathBuilder builder;
        final Object result;
        final int type;

        public Step(int type) {
            this.path = null;
            this.builder = null;
            this.result = null;
            this.type = type;
        }

        public Step(Object result, int type) {
            this.path = null;
            this.builder = null;
            this.result = result;
            this.type = type;
        }

        public Step(ScreenPath path, int type) {
            Preconditions.checkNotNull(path, "ScreenPath cannot be null");
            this.path = path;
            this.builder = null;
            this.result = null;
            this.type = type;
        }

        public Step(PathBuilder builder, int type) {
            Preconditions.checkNotNull(builder, "PathBuilder cannot be null");
            this.path = null;
            this.builder = builder;
            this.result = null;
            this.type = type;
        }
    }
}