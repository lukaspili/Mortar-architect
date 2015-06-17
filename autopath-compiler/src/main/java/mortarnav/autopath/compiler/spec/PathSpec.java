package mortarnav.autopath.compiler.spec;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PathSpec extends AbstractSpec {

    private TypeName viewTypeName;
    private final List<ConstructorSpec> constructors;

    public PathSpec(ClassName className) {
        super(className);
        constructors = new ArrayList<>();
    }

    public TypeName getViewTypeName() {
        return viewTypeName;
    }

    public void setViewTypeName(TypeName viewTypeName) {
        this.viewTypeName = viewTypeName;
    }

    public List<ConstructorSpec> getConstructors() {
        return constructors;
    }
}
