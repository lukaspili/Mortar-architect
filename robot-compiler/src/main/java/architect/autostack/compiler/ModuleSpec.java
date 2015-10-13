package architect.autostack.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ModuleSpec {

    private final ClassName className;
    private TypeName presenterTypeName;
    private AnnotationSpec scopeAnnotationSpec;
    private final List<ParameterSpec> daggerParameterSpecs;
    private final List<ParameterSpec> screenParameterSpecs;
    private final List<ParameterSpec> allParameterSpecs;

    public ModuleSpec(ClassName className) {
        this.className = className;
        daggerParameterSpecs = new ArrayList<>();
        screenParameterSpecs = new ArrayList<>();
        allParameterSpecs = new ArrayList<>();
    }

    public ClassName getClassName() {
        return className;
    }

    public TypeName getPresenterTypeName() {
        return presenterTypeName;
    }

    public void setPresenterTypeName(TypeName presenterTypeName) {
        this.presenterTypeName = presenterTypeName;
    }

    public AnnotationSpec getScopeAnnotationSpec() {
        return scopeAnnotationSpec;
    }

    public void setScopeAnnotationSpec(AnnotationSpec scopeAnnotationSpec) {
        this.scopeAnnotationSpec = scopeAnnotationSpec;
    }

    public List<ParameterSpec> getDaggerParameterSpecs() {
        return daggerParameterSpecs;
    }

    public List<ParameterSpec> getScreenParameterSpecs() {
        return screenParameterSpecs;
    }

    public List<ParameterSpec> getAllParameterSpecs() {
        return allParameterSpecs;
    }
}
