package mortarnav.autopath.compiler.spec;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PathSpec extends AbstractSpec {

    private final TypeName targetTypeName;
    private final List<ConstructorSpec> constructors;
    private final List<ParamSpec> fields;
    private TypeName viewTypeName;

    public PathSpec(ClassName className, TypeName targetTypeName) {
        super(className);
        this.targetTypeName = targetTypeName;
        constructors = new ArrayList<>();
        fields = new ArrayList<>();
    }

    public TypeName getTargetTypeName() {
        return targetTypeName;
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

    public List<ParamSpec> getFields() {
        return fields;
    }
}
