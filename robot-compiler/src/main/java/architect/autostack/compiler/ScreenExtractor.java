package architect.autostack.compiler;

import com.google.auto.common.MoreElements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Scope;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import architect.robot.AutoScreen;
import architect.robot.NavigationParam;
import architect.robot.NavigationResult;
import architect.robot.ScreenData;
import autodagger.compiler.utils.AutoComponentExtractorUtil;
import processorworkflow.AbstractExtractor;
import processorworkflow.Errors;
import processorworkflow.ExtractorUtils;
import processorworkflow.Logger;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenExtractor extends AbstractExtractor {

    private static final String COMPONENT = "component";
    private static final String PATH_VIEW = "pathView";
    private static final String SUBSCREENS = "subScreens";

    private AnnotationMirror scopeAnnotationTypeMirror;
    private AnnotationMirror componentAnnotationTypeMirror;
    private TypeMirror componentDependency;
    private TypeMirror pathViewTypeMirror;
    private int pathLayout;
    private VariableElement navigationResultElement;
    private List<SubscreensExtractor> subscreensExtractors;
    private List<VariableElement> constructorsParamtersElements;
    private List<VariableElement> screenDataElements;
    private Map<Integer, List<VariableElement>> navigationParamsElements;

    public ScreenExtractor(Element element, Types types, Elements elements, Errors errors) {
        super(element, types, elements, errors);

        Logger.d("Extract %s", element.getSimpleName());
        extract();
    }

    @Override
    public void extract() {
        componentAnnotationTypeMirror = ExtractorUtils.getValueFromAnnotation(element, AutoScreen.class, COMPONENT);
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

        pathViewTypeMirror = ExtractorUtils.getValueFromAnnotation(element, AutoScreen.class, PATH_VIEW);
        pathLayout = element.getAnnotation(AutoScreen.class).pathLayout();

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

        // subScreens
        List<AnnotationValue> values = ExtractorUtils.getValueFromAnnotation(element, AutoScreen.class, SUBSCREENS);
        if (values != null && !values.isEmpty()) {
            subscreensExtractors = new ArrayList<>(values.size());
            for (AnnotationValue value : values) {
                subscreensExtractors.add(new SubscreensExtractor(value));
            }
        }

        navigationParamsElements = new HashMap<>();
        screenDataElements = new ArrayList<>();

        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.FIELD) {
                // nav result
                if (MoreElements.isAnnotationPresent(enclosedElement, NavigationResult.class)) {
                    if (navigationResultElement != null) {
                        errors.addInvalid("Cannot have several @NavigationResult");
                        return;
                    }

                    navigationResultElement = MoreElements.asVariable(enclosedElement);
                }
                // nav param
                else if (MoreElements.isAnnotationPresent(enclosedElement, NavigationParam.class)) {
                    int[] group = enclosedElement.getAnnotation(NavigationParam.class).group();
                    if (group == null || group.length == 0) {
                        addNavigationParam(0, enclosedElement);
                        continue;
                    }

                    for (int gr : group) {
                        addNavigationParam(gr, enclosedElement);
                    }
                }
                // screen data
                else if (MoreElements.isAnnotationPresent(enclosedElement, ScreenData.class)) {
                    screenDataElements.add(MoreElements.asVariable(enclosedElement));
                }
            }
        }
    }

    private void addNavigationParam(int group, Element element) {
        List<VariableElement> elements = navigationParamsElements.get(group);
        if (elements == null) {
            elements = new ArrayList<>();
            navigationParamsElements.put(group, elements);
        }
        elements.add(MoreElements.asVariable(element));
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

    public VariableElement getNavigationResultElement() {
        return navigationResultElement;
    }

    public List<VariableElement> getConstructorsParamtersElements() {
        return constructorsParamtersElements;
    }

    public List<SubscreensExtractor> getSubscreensExtractors() {
        return subscreensExtractors;
    }

    public List<VariableElement> getScreenDataElements() {
        return screenDataElements;
    }

    public Map<Integer, List<VariableElement>> getNavigationParamsElements() {
        return navigationParamsElements;
    }
}
