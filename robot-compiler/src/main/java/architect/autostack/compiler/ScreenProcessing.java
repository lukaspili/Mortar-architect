package architect.autostack.compiler;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import architect.robot.AutoScreen;
import autodagger.AutoComponent;
import autodagger.compiler.utils.AutoComponentClassNameUtil;
import processorworkflow.AbstractComposer;
import processorworkflow.AbstractProcessing;
import processorworkflow.Errors;
import processorworkflow.ProcessingBuilder;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenProcessing extends AbstractProcessing<ScreenSpec, Void> {

    public ScreenProcessing(Elements elements, Types types, Errors errors, Void aVoid) {
        super(elements, types, errors, aVoid);
    }

    @Override
    public Set<Class<? extends Annotation>> supportedAnnotations() {
        Set set = ImmutableSet.of(AutoScreen.class);
        return set;
    }

    @Override
    public boolean processElement(Element element, Errors.ElementErrors elementErrors) {
        ScreenExtractor extractor = new ScreenExtractor(element, types, elements, errors);
        if (errors.hasErrors()) {
            return false;
        }

        ScreenSpec spec = new ElementBuilder(extractor, errors).build();
        if (errors.hasErrors()) {
            return false;
        }

        specs.add(spec);
        return true;
    }

    @Override
    public AbstractComposer<ScreenSpec> createComposer() {
        return new ScreenComposer(specs);
    }

    private class ElementBuilder extends ProcessingBuilder<ScreenExtractor, ScreenSpec> {

        public ElementBuilder(ScreenExtractor extractor, Errors errors) {
            super(extractor, errors);
        }

        @Override
        protected ScreenSpec build() {
            ScreenSpec spec = new ScreenSpec(buildClassName(extractor.getElement()));
            spec.setParentComponentTypeName(TypeName.get(extractor.getComponentDependency()));

            TypeName presenterTypeName = TypeName.get(extractor.getElement().asType());
            spec.setPresenterTypeName(presenterTypeName);

            ClassName moduleClassName = ClassName.get(spec.getClassName().packageName(), spec.getClassName().simpleName(), "Module");

            AnnotationSpec.Builder builder = AnnotationSpec.get(extractor.getComponentAnnotationTypeMirror()).toBuilder();
            builder.addMember("target", "$T.class", presenterTypeName);
            builder.addMember("modules", "$T.class", moduleClassName);
            spec.setComponentAnnotationSpec(builder.build());

            spec.setDaggerComponentTypeName(ClassName.get(spec.getClassName().packageName(), String.format("Dagger%sComponent", spec.getClassName().simpleName())));

            // dagger2 builder dependency method name and type can have 3 diff values
            // - name and type of the generated scope if dependency is annotated with @AutoScreen
            // - name and type of the target if dependency is annotated with @AutoComponent (valid also for #2, so check #2 condition first)
            // - name and type of the class if dependency is a manually written component
            String methodName;
            TypeName typeName;
            TypeName parentTypeName;
            Element daggerDependencyElement = MoreTypes.asElement(extractor.getComponentDependency());
            if (MoreElements.isAnnotationPresent(daggerDependencyElement, AutoScreen.class)) {
                ClassName daggerDependencyScopeClassName = buildClassName(daggerDependencyElement);
                ClassName daggerDependencyClassName = AutoComponentClassNameUtil.getComponentClassName(daggerDependencyScopeClassName);
                methodName = StringUtils.uncapitalize(daggerDependencyClassName.simpleName());
                typeName = daggerDependencyClassName;
                parentTypeName = ClassName.get(daggerDependencyClassName.packageName(), daggerDependencyClassName.simpleName().substring(0, daggerDependencyClassName.simpleName().length() - "Component".length()));
            } else if (MoreElements.isAnnotationPresent(daggerDependencyElement, AutoComponent.class)) {
                ClassName daggerDependencyClassName = AutoComponentClassNameUtil.getComponentClassName(daggerDependencyElement);
                methodName = StringUtils.uncapitalize(daggerDependencyElement.getSimpleName().toString()) + "Component";
                typeName = daggerDependencyClassName;
                parentTypeName = ClassName.get(daggerDependencyClassName.packageName(), daggerDependencyClassName.simpleName().substring(0, daggerDependencyClassName.simpleName().length() - "Component".length()));
            } else {
                methodName = StringUtils.uncapitalize(daggerDependencyElement.getSimpleName().toString());
                typeName = TypeName.get(extractor.getComponentDependency());
                parentTypeName = typeName;
            }
            spec.setDaggerComponentBuilderDependencyTypeName(typeName);
            spec.setDaggerComponentBuilderDependencyMethodName(methodName);
            spec.setParentTypeName(parentTypeName);

//            if (extractor.getScopeAnnotationTypeMirror() != null) {
//                spec.setScopeAnnotationSpec(AnnotationSpec.get(extractor.getScopeAnnotationTypeMirror()));
//            }

            if (extractor.getPathViewTypeMirror() != null) {
                spec.setPathViewTypeName(TypeName.get(extractor.getPathViewTypeMirror()));
            }

            if (extractor.getPathLayout() != 0) {
                spec.setPathLayout(extractor.getPathLayout());
            }

            if (extractor.getSubscreensExtractors() != null && !extractor.getSubscreensExtractors().isEmpty()) {
                List<FieldSpec> subscreenSpecs = new ArrayList<>(extractor.getSubscreensExtractors().size());
                for (SubscreensExtractor subscreensExtractor : extractor.getSubscreensExtractors()) {
                    Element e = MoreTypes.asElement(subscreensExtractor.getTypeMirror());
                    TypeName tn;
                    if (MoreElements.isAnnotationPresent(e, AutoScreen.class)) {
                        tn = buildClassName(e);
                    } else {
                        tn = TypeName.get(subscreensExtractor.getTypeMirror());
                    }
                    subscreenSpecs.add(FieldSpec.builder(tn, ScreenComposer.SUBSCREEN_FIELD_PREFIX + subscreensExtractor.getName()).build());
                }
                spec.setSubscreenSpecs(subscreenSpecs);
            }

            List<FieldSpec> allFields = new ArrayList<>();

            if (extractor.getNavigationResultElement() != null) {
                spec.setNavigationResultSpec(FieldSpec.builder(TypeName.get(extractor.getNavigationResultElement().asType()), extractor.getNavigationResultElement().getSimpleName().toString()).build());
                allFields.add(spec.getNavigationResultSpec());
            }

            if (extractor.getNavigationParamsElements() != null && !extractor.getNavigationParamsElements().isEmpty()) {
                Set<String> addFields = new HashSet<>();
                List<FieldSpec> fieldSpecs = new ArrayList<>();
                List<List<ParameterSpec>> paramSpecs = new ArrayList<>();

                for (Map.Entry<Integer, List<VariableElement>> entry : extractor.getNavigationParamsElements().entrySet()) {

                    List<ParameterSpec> p = new ArrayList<>();
                    for (VariableElement paramElement : entry.getValue()) {
                        String name = paramElement.getSimpleName().toString();
                        TypeName paramTypeName = TypeName.get(paramElement.asType());
                        p.add(ParameterSpec.builder(paramTypeName, name).build());

                        if (!addFields.contains(name)) {
                            addFields.add(name);
                            FieldSpec fieldSpec = FieldSpec.builder(paramTypeName, name).build();
                            fieldSpecs.add(fieldSpec);
                            allFields.add(fieldSpec);
                        }
                    }
                    paramSpecs.add(p);
                }
                spec.setNavigationParamFieldSpecs(fieldSpecs);
                spec.setNavigationParamConstructorSpecs(paramSpecs);
            }

            if (extractor.getScreenDataElements() != null && !extractor.getScreenDataElements().isEmpty()) {
                List<FieldSpec> screenDataSpecs = new ArrayList<>();
                for (VariableElement screenDataElement : extractor.getScreenDataElements()) {
                    screenDataSpecs.add(FieldSpec.builder(TypeName.get(screenDataElement.asType()), ScreenComposer.DATA_FIELD_PREFIX + screenDataElement.getSimpleName().toString()).build());
                }
                spec.setScreenDataSpecs(screenDataSpecs);
            }

            ModuleSpec moduleSpec = new ModuleSpec(moduleClassName);
            moduleSpec.setPresenterTypeName(presenterTypeName);
//            moduleSpec.setScopeAnnotationSpec(spec.getScopeAnnotationSpec());


            for (ConstructorParameterExtractor e : extractor.getConstructorsParameterExctractors()) {
                String screenFieldName = getMatchingFieldName(e.getName(), spec);

                ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.get(e.getType()), screenFieldName != null ? screenFieldName : e.getName()).build();
                moduleSpec.getAllParameterSpecs().add(parameterSpec);

                if (screenFieldName != null) {
                    moduleSpec.getScreenParameterSpecs().add(parameterSpec);
                } else {
                    moduleSpec.getDaggerParameterSpecs().add(parameterSpec);
                }
            }

            spec.setModuleSpec(moduleSpec);

            return spec;
        }

        /**
         * Get a matching field name in navigation result, navigation params, screen data
         */
        private String getMatchingFieldName(String parameter, ScreenSpec spec) {
            if (spec.getNavigationResultSpec() != null) {
                String name = getMatchingFieldName(parameter, spec.getNavigationResultSpec().name);
                if (name != null) {
                    return name;
                }
            }

            if (spec.getNavigationParamFieldSpecs() != null) {
                for (FieldSpec fieldSpec : spec.getNavigationParamFieldSpecs()) {
                    String name = getMatchingFieldName(parameter, fieldSpec.name);
                    if (name != null) {
                        return name;
                    }
                }
            }

            if (spec.getScreenDataSpecs() != null) {
                for (FieldSpec fieldSpec : spec.getScreenDataSpecs()) {
                    String name = getMatchingFieldName(parameter, fieldSpec.name.substring(ScreenComposer.DATA_FIELD_PREFIX.length()));
                    if (name != null) {
                        return ScreenComposer.DATA_FIELD_PREFIX + name;
                    }
                }
            }

            return null;
        }

        /**
         * Get a matching name between the parameter name and the field name
         * Take in account the hungarian notation
         */
        private String getMatchingFieldName(String parameter, String field) {
            if (field.equals(parameter)) {
                return field;
            }

            if (field.startsWith("m") && field.length() > 1) {
                if (parameter.equals(StringUtils.uncapitalize(field.substring(1)))) {
                    return field;
                }
            }

            return null;
        }

        private ClassName buildClassName(Element element) {
            String name = element.getSimpleName().toString();

            // try to remove Presenter at the end of the name
            String newName = removeEndingName(name, "Presenter");
            if (newName == null) {
                errors.addInvalid("Class name " + newName);
            }

            String pkg = MoreElements.getPackage(element).getQualifiedName().toString();
            if (StringUtils.isBlank(pkg)) {
                errors.addInvalid("Package name " + pkg);
            }
            pkg = pkg + ".screen";

            return ClassName.get(pkg, newName + "Screen");
        }

        private String removeEndingName(String text, String term) {
            if (StringUtils.isBlank(text)) {
                return null;
            }

            int index = text.lastIndexOf(term);
            if (index >= 0) {
                text = text.substring(0, index);
                if (StringUtils.isBlank(text)) {
                    return null;
                }
            }

            return text;
        }
    }
}
