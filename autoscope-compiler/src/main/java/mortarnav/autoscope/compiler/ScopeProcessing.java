package mortarnav.autoscope.compiler;

import com.google.auto.common.MoreElements;
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

import mortarnav.autoscope.AutoScope;
import mortarnav.autoscope.FromNav;
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
        return AutoScope.class;
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
            ScopeSpec spec = new ScopeSpec(buildClassName());
            spec.setParentComponentTypeName(TypeName.get(extractor.getComponentDependency()));

            TypeName presenterTypeName = TypeName.get(extractor.getElement().asType());
            ClassName moduleClassName = ClassName.get(spec.getClassName().packageName(), spec.getClassName().simpleName(), "Module");

            AnnotationSpec.Builder builder = AnnotationSpec.get(extractor.getComponentAnnotationTypeMirror()).toBuilder();
            builder.addMember("target", "$T.class", presenterTypeName);
            builder.addMember("modules", "$T.class", moduleClassName);
            spec.setComponentAnnotationSpec(builder.build());

            spec.setDaggerComponentTypeName(ClassName.get(spec.getClassName().packageName(), String.format("Dagger%sComponent", spec.getClassName().simpleName())));

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

                if (!MoreElements.isAnnotationPresent(e, FromNav.class)) {
                    moduleSpec.getProvideParameters().add(parameterSpec);
                }
            }

            spec.setModuleSpec(moduleSpec);

            return spec;
        }

        private ClassName buildClassName() {
            String name = extractor.getElement().getSimpleName().toString();

            // try to remove NavigationScope at the end of the name
            String newName = removeEndingName(name, "Presenter");
            if (newName == null) {
                errors.addInvalid("Class name " + newName);
            }

            String pkg = MoreElements.getPackage(extractor.getElement()).getQualifiedName().toString();
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
