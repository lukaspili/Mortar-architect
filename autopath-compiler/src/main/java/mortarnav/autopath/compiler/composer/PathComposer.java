package mortarnav.autopath.compiler.composer;

import com.squareup.javapoet.JavaFile;

import java.util.List;

import mortarnav.autopath.compiler.spec.PathSpec;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PathComposer extends AbstractComposer<PathSpec> {

    public PathComposer(List<PathSpec> specs) {
        super(specs);
    }

    @Override
    protected JavaFile compose(PathSpec spec) {
        return null;
    }
}
