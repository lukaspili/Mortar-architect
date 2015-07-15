package architect.autostack.compiler;

import com.google.auto.common.MoreElements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Scope;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import architect.robot.AutoStackable;
import autodagger.compiler.utils.AutoComponentExtractorUtil;
import processorworkflow.AbstractExtractor;
import processorworkflow.Errors;
import processorworkflow.ExtractorUtils;
import processorworkflow.Logger;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScopeExtractor extends AbstractExtractor {

    private static final String COMPONENT = "component";
    private static final String PATH_VIEW = "pathWithView";

    private AnnotationMirror scopeAnnotationTypeMirror;
    private AnnotationMirror componentAnnotationTypeMirror;
    private TypeMirror componentDependency;
    private TypeMirror pathViewTypeMirror;
    private int pathLayout;
    //    private List<VariableElement> fromPathFieldsElements;
    private List<VariableElement> constructorsParamtersElements;

    public ScopeExtractor(Element element, Types types, Elements elements, Errors errors) {
        super(element, types, elements, errors);

        Logger.d("Extract %s", element.getSimpleName());
        extract();
    }

    @Override
    public void extract() {
        componentAnnotationTypeMirror = ExtractorUtils.getValueFromAnnotation(element, AutoStackable.class, COMPONENT);
        if (componentAnnotationTypeMirror == null) {
            errors.addMissing("@AutoComponent");
            return;
        }

        // get dependency from @AutoComponent
        List<TypeMirror> deps = AutoComponentExtractorUtil.getDependencies(componentAnnotationTypeMirror, errors);
        if (deps.size() != 1) {
            errors.addInvalid("@AutoComponent must have only 1 dependency");
            return;
        }
        componentDependency = deps.get(0);

        pathViewTypeMirror = ExtractorUtils.getValueFromAnnotation(element, AutoStackable.class, PATH_VIEW);
        pathLayout = element.getAnnotation(AutoStackable.class).pathWithLayout();
        scopeAnnotationTypeMirror = findScope();

//        fromPathFieldsElements = new ArrayList<>();
        constructorsParamtersElements = new ArrayList<>();
        int constructorsCount = 0;
        for (Element enclosedElement : element.getEnclosedElements()) {
//            if (enclosedElement.getKind() == ElementKind.FIELD &&
//                    MoreElements.isAnnotationPresent(enclosedElement, FromPath.class)) {
//                Logger.d("Get field : %s", enclosedElement.getSimpleName());
//                fromPathFieldsElements.add(MoreElements.asVariable(enclosedElement));
//            } else

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

    public TypeMirror getPathViewTypeMirror() {
        return pathViewTypeMirror;
    }

    public TypeMirror getComponentDependency() {
        return componentDependency;
    }

    public int getPathLayout() {
        return pathLayout;
    }

    //    public List<VariableElement> getFromPathFieldsElements() {
//        return fromPathFieldsElements;
//    }

    public List<VariableElement> getConstructorsParamtersElements() {
        return constructorsParamtersElements;
    }
}
