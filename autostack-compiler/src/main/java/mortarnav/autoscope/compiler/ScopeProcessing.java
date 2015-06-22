package mortarnav.autoscope.compiler;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import autodagger.AutoComponent;
import autodagger.compiler.utils.AutoComponentClassNameUtil;
import mortarnav.autoscope.AutoStack;
import mortarnav.autoscope.StackParam;
import mortarnav.processor.AbstractComposer;
import mortarnav.processor.AbstractProcessing;
import mortarnav.processor.Errors;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScopeProcessing extends AbstractProcessing<ScopeSpec> {

    public ScopeProcessing(Elements elements, Types types, Errors errors) {
        super(elements, types, errors);
    }

    @Override
    public Class<? extends Annotation> supportedAnnotation() {
        return AutoStack.class;
    }

    @Override
    public boolean processElement(Element element, Errors.ElementErrors elementErrors) {
        ScopeExtractor extractor = new ScopeExtractor(element, types, elements, errors);
        if (errors.hasErrors()) {
            return false;
        }

        ScopeSpec spec = new ElementBuilder(extractor, errors).build();
        if (errors.hasErrors()) {
            return false;
        }

        specs.add(spec);
        return true;
    }

    @Override
    public AbstractComposer<ScopeSpec> createComposer() {
        return new ScopeComposer(specs);
    }

    private class ElementBuilder {

        private final ScopeExtractor extractor;
        private final Errors.ElementErrors errors;

        public ElementBuilder(ScopeExtractor extractor, Errors errors) {
            this.extractor = extractor;
            this.errors = errors.getFor(extractor.getElement());
        }

        private ScopeSpec build() {
            ScopeSpec spec = new ScopeSpec(buildClassName(extractor.getElement()));
            spec.setParentComponentTypeName(TypeName.get(extractor.getComponentDependency()));

            TypeName presenterTypeName = TypeName.get(extractor.getElement().asType());
            ClassName moduleClassName = ClassName.get(spec.getClassName().packageName(), spec.getClassName().simpleName(), "Module");

            AnnotationSpec.Builder builder = AnnotationSpec.get(extractor.getComponentAnnotationTypeMirror()).toBuilder();
            builder.addMember("target", "$T.class", presenterTypeName);
            builder.addMember("modules", "$T.class", moduleClassName);
            spec.setComponentAnnotationSpec(builder.build());

            spec.setDaggerComponentTypeName(ClassName.get(spec.getClassName().packageName(), String.format("Dagger%sComponent", spec.getClassName().simpleName())));

            // dagger2 builder dependency method name and type can have 3 diff values
            // - name and type of the generated scope if dependency is annotated with @AutoScope
            // - name and type of the target if dependency is annotated with @AutoComponent (valid also for #2, so check #2 condition first)
            // - name and type of the class if dependency is a manually written component
            String methodName;
            TypeName typeName;
            Element daggerDependencyElement = MoreTypes.asElement(extractor.getComponentDependency());
            if (MoreElements.isAnnotationPresent(daggerDependencyElement, AutoStack.class)) {
                ClassName daggerDependencyScopeClassName = buildClassName(daggerDependencyElement);
                ClassName daggerDependencyClassName = AutoComponentClassNameUtil.getComponentClassName(daggerDependencyScopeClassName);
                methodName = StringUtils.uncapitalize(daggerDependencyClassName.simpleName());
                typeName = daggerDependencyClassName;
            } else if (MoreElements.isAnnotationPresent(daggerDependencyElement, AutoComponent.class)) {
                methodName = StringUtils.uncapitalize(daggerDependencyElement.getSimpleName().toString()) + "Component";
                typeName = AutoComponentClassNameUtil.getComponentClassName(daggerDependencyElement);
            } else {
                methodName = StringUtils.uncapitalize(daggerDependencyElement.getSimpleName().toString());
                typeName = TypeName.get(extractor.getComponentDependency());
            }
            spec.setDaggerComponentBuilderDependencyTypeName(typeName);
            spec.setDaggerComponentBuilderDependencyMethodName(methodName);

            if (extractor.getScopeAnnotationTypeMirror() != null) {
                spec.setScopeAnnotationSpec(AnnotationSpec.get(extractor.getScopeAnnotationTypeMirror()));
            }

            if (extractor.getPathAnnotationTypeMirror() != null) {
                spec.setPathAnnotationSpec(AnnotationSpec.get(extractor.getPathAnnotationTypeMirror()));
            }

            ModuleSpec moduleSpec = new ModuleSpec(moduleClassName);
            moduleSpec.setPresenterTypeName(presenterTypeName);
            moduleSpec.setScopeAnnotationSpec(spec.getScopeAnnotationSpec());

            for (VariableElement e : extractor.getConstructorsParamtersElements()) {
                ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.get(e.asType()), e.getSimpleName().toString()).build();
                moduleSpec.getPresenterArgs().add(parameterSpec);

                if (!MoreElements.isAnnotationPresent(e, StackParam.class)) {
                    moduleSpec.getProvideParameters().add(parameterSpec);
                }
            }

            spec.setModuleSpec(moduleSpec);

            return spec;
        }

        private ClassName buildClassName(Element element) {
            String name = element.getSimpleName().toString();

            // try to remove NavigationScope at the end of the name
            String newName = removeEndingName(name, "Presenter");
            if (newName == null) {
                errors.addInvalid("Class name " + newName);
            }

            String pkg = MoreElements.getPackage(element).getQualifiedName().toString();
            if (StringUtils.isBlank(pkg)) {
                errors.addInvalid("Package name " + pkg);
            }
            pkg = pkg + ".scope";

            return ClassName.get(pkg, newName + "Scope");
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
