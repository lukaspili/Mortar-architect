package architect.autostack.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScopeSpec extends architect.processor.AbstractSpec {

    private ModuleSpec moduleSpec;
    private String daggerComponentBuilderDependencyMethodName;
    private TypeName daggerComponentBuilderDependencyTypeName;
    private TypeName daggerComponentTypeName;
    private TypeName parentComponentTypeName;
    private AnnotationSpec scopeAnnotationSpec;
    private AnnotationSpec componentAnnotationSpec;
    private AnnotationSpec pathAnnotationSpec;

    public ScopeSpec(ClassName className) {
        super(className);
    }

    public ModuleSpec getModuleSpec() {
        return moduleSpec;
    }

    public String getDaggerComponentBuilderDependencyMethodName() {
        return daggerComponentBuilderDependencyMethodName;
    }

    public void setDaggerComponentBuilderDependencyMethodName(String daggerComponentBuilderDependencyMethodName) {
        this.daggerComponentBuilderDependencyMethodName = daggerComponentBuilderDependencyMethodName;
    }

    public TypeName getDaggerComponentBuilderDependencyTypeName() {
        return daggerComponentBuilderDependencyTypeName;
    }

    public void setDaggerComponentBuilderDependencyTypeName(TypeName daggerComponentBuilderDependencyTypeName) {
        this.daggerComponentBuilderDependencyTypeName = daggerComponentBuilderDependencyTypeName;
    }

    public void setModuleSpec(ModuleSpec moduleSpec) {
        this.moduleSpec = moduleSpec;
    }

    public TypeName getDaggerComponentTypeName() {
        return daggerComponentTypeName;
    }

    public void setDaggerComponentTypeName(TypeName daggerComponentTypeName) {
        this.daggerComponentTypeName = daggerComponentTypeName;
    }

    public TypeName getParentComponentTypeName() {
        return parentComponentTypeName;
    }

    public void setParentComponentTypeName(TypeName parentComponentTypeName) {
        this.parentComponentTypeName = parentComponentTypeName;
    }

    public AnnotationSpec getScopeAnnotationSpec() {
        return scopeAnnotationSpec;
    }

    public void setScopeAnnotationSpec(AnnotationSpec scopeAnnotationSpec) {
        this.scopeAnnotationSpec = scopeAnnotationSpec;
    }

    public AnnotationSpec getComponentAnnotationSpec() {
        return componentAnnotationSpec;
    }

    public void setComponentAnnotationSpec(AnnotationSpec componentAnnotationSpec) {
        this.componentAnnotationSpec = componentAnnotationSpec;
    }

    public AnnotationSpec getPathAnnotationSpec() {
        return pathAnnotationSpec;
    }

    public void setPathAnnotationSpec(AnnotationSpec pathAnnotationSpec) {
        this.pathAnnotationSpec = pathAnnotationSpec;
    }
}
