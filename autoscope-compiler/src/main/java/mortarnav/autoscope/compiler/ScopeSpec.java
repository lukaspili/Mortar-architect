package mortarnav.autoscope.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import mortarnav.processor.AbstractSpec;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScopeSpec extends AbstractSpec {

    private ModuleSpec moduleSpec;
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
