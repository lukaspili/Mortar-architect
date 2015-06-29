package architect.autopath.compiler.spec;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PathSpec {

    private final ClassName className;
    private final TypeName targetTypeName;
    private final List<ConstructorSpec> constructors;
    private final List<architect.autopath.compiler.spec.ParamSpec> fields;
    private TypeName viewTypeName;

    public PathSpec(ClassName className, TypeName targetTypeName) {
        this.className = className;
        this.targetTypeName = targetTypeName;
        constructors = new ArrayList<>();
        fields = new ArrayList<>();
    }

    public ClassName getClassName() {
        return className;
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

    public List<architect.autopath.compiler.spec.ParamSpec> getFields() {
        return fields;
    }
}
