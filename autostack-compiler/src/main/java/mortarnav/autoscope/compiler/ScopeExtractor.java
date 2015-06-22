package mortarnav.autoscope.compiler;

import com.google.auto.common.MoreElements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Scope;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import autodagger.AutoComponent;
import mortarnav.autoscope.AutoStack;
import mortarnav.processor.AbstractExtractor;
import mortarnav.processor.Errors;
import mortarnav.processor.ExtractorUtils;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScopeExtractor extends AbstractExtractor {

    private static final String COMPONENT = "component";
    private static final String COMPONENT_DEPENDENCIES = "dependencies";
    private static final String PATH = "path";

    private AnnotationMirror scopeAnnotationTypeMirror;
    private AnnotationMirror componentAnnotationTypeMirror;
    private AnnotationMirror pathAnnotationTypeMirror;
    private TypeMirror componentDependency;
    private List<VariableElement> constructorsParamtersElements;

    public ScopeExtractor(Element element, Types types, Elements elements, Errors errors) {
        super(element, types, elements, errors);
    }

    @Override
    protected void extract() {
        componentAnnotationTypeMirror = ExtractorUtils.getValueFromAnnotation(element, AutoStack.class, COMPONENT);
        if (componentAnnotationTypeMirror == null) {
            errors.addMissing("@AutoComponent");
            return;
        }

        List<TypeMirror> deps = findTypeMirrors(componentAnnotationTypeMirror, COMPONENT_DEPENDENCIES);
        if (deps == null || deps.size() != 1) {
            errors.addInvalid("@AutoComponent must have only 1 dependency");
            return;
        }
        componentDependency = deps.get(0);

        pathAnnotationTypeMirror = ExtractorUtils.getValueFromAnnotation(element, AutoStack.class, PATH);
        scopeAnnotationTypeMirror = findScope();

        constructorsParamtersElements = new ArrayList<>();
        int constructorsCount = 0;
        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.CONSTRUCTOR) {
                constructorsCount++;
                for (VariableElement variableElement : MoreElements.asExecutable(enclosedElement).getParameters()) {
                    constructorsParamtersElements.add(variableElement);
                }
            }
        }

        if (constructorsCount > 1) {
            errors.addInvalid("Cannot have several constructors");
            return;
        }
    }

    private List<TypeMirror> findTypeMirrors(AnnotationMirror annotationMirror, String name) {
        List<TypeMirror> typeMirrors = new ArrayList<>();
        List<AnnotationValue> values = ExtractorUtils.getValueFromAnnotation(annotationMirror, AutoComponent.class, name);
        if (values != null) {
            for (AnnotationValue value : values) {
                try {
                    TypeMirror tm = (TypeMirror) value.getValue();
                    typeMirrors.add(tm);
                } catch (Exception e) {
                    errors.addInvalid("@AutoComponent dependency (did your reference an auto generated class? use rather the target class)");
                    break;
                }
            }
        }

        return typeMirrors;
    }

    /**
     * Find annotation that is itself annoted with @Scope
     * If there is one, it will be later applied on the generated component
     * Otherwise the component will be unscoped
     * Throw error if more than one scope annotation found
     */
    private AnnotationMirror findScope() {
        AnnotationMirror annotationTypeMirror = null;

        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            Element annotationElement = annotationMirror.getAnnotationType().asElement();
            if (MoreElements.isAnnotationPresent(annotationElement, Scope.class)) {
                // already found one scope
                if (annotationTypeMirror != null) {
                    errors.addInvalid("Several dagger scopes on same element are not allowed");
                    continue;
                }

                annotationTypeMirror = annotationMirror;
            }
        }

        return annotationTypeMirror;
    }

    public AnnotationMirror getScopeAnnotationTypeMirror() {
        return scopeAnnotationTypeMirror;
    }

    public AnnotationMirror getComponentAnnotationTypeMirror() {
        return componentAnnotationTypeMirror;
    }

    public AnnotationMirror getPathAnnotationTypeMirror() {
        return pathAnnotationTypeMirror;
    }

    public TypeMirror getComponentDependency() {
        return componentDependency;
    }

    public List<VariableElement> getConstructorsParamtersElements() {
        return constructorsParamtersElements;
    }
}
