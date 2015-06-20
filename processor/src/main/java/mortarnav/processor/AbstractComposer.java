package mortarnav.processor;

import com.squareup.javapoet.JavaFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class AbstractComposer<T extends AbstractSpec> {

    private List<T> specs;

    public AbstractComposer(List<T> specs) {
        this.specs = specs;
    }

    public List<JavaFile> compose() {
        List<JavaFile> javaFiles = new ArrayList<>(specs.size());
        for (T spec : specs) {
            javaFiles.add(compose(spec));
        }

        return javaFiles;
    }

    protected abstract JavaFile compose(T spec);


}
