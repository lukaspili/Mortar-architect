package mortarnav.autoscope.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import java.util.LinkedList;
import java.util.List;

import mortarnav.processor.AbstractSpec;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ModuleSpec extends AbstractSpec {

    private TypeName presenterTypeName;
    private AnnotationSpec scopeAnnotationSpec;
    private final List<ParameterSpec> provideParameters;
    private final List<ParameterSpec> presenterArgs;

    public ModuleSpec(ClassName className) {
        super(className);

        provideParameters = new LinkedList<>();
        presenterArgs = new LinkedList<>();
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

    public List<ParameterSpec> getProvideParameters() {
        return provideParameters;
    }

    public List<ParameterSpec> getPresenterArgs() {
        return presenterArgs;
    }
}
