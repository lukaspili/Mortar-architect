package architect.autopath.compiler.processing;

import com.google.auto.common.MoreElements;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import architect.autopath.AutoPath;
import architect.autopath.compiler.composer.PathComposer;
import architect.autopath.compiler.extractor.PathExtractor;
import architect.autopath.compiler.spec.ConstructorSpec;
import architect.autopath.compiler.spec.PathSpec;
import processorworkflow.AbstractComposer;
import processorworkflow.AbstractProcessing;
import processorworkflow.Errors;
import processorworkflow.ProcessingBuilder;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PathProcessing extends AbstractProcessing<PathSpec, Void> {

    public PathProcessing(Elements elements, Types types, Errors errors, Void aVoid) {
        super(elements, types, errors, aVoid);
    }

    @Override
    public Set<Class<? extends Annotation>> supportedAnnotations() {
        Set set = ImmutableSet.of(AutoPath.class);
        return set;
    }

    @Override
    public boolean processElement(Element element, Errors.ElementErrors elementErrors) {
        PathExtractor extractor = new architect.autopath.compiler.extractor.PathExtractor(element, types, elements, errors);
        if (errors.hasErrors()) {
            return false;
        }

        PathSpec spec = new ElementBuilder(extractor, errors).build();
        if (errors.hasErrors()) {
            return false;
        }

        specs.add(spec);
        return true;
    }

    @Override
    public AbstractComposer<PathSpec> createComposer() {
        return new PathComposer(specs);
    }

    private class ElementBuilder extends ProcessingBuilder<PathExtractor, PathSpec> {

        public ElementBuilder(PathExtractor extractor, Errors errors) {
            super(extractor, errors);
        }

        @Override
        protected PathSpec build() {
            PathSpec spec = new architect.autopath.compiler.spec.PathSpec(buildClassName(), TypeName.get(extractor.getElement().asType()));
            spec.setViewTypeName(TypeName.get(extractor.getViewTypeMirror()));

            boolean defaultConstructorNotPublic = false;
            int nonDefaultConstructors = 0;
            for (ExecutableElement e : extractor.getConstructorElements()) {
                if (e.getParameters().isEmpty()) {
                    if (e.getModifiers().contains(Modifier.PUBLIC)) {
                        spec.getConstructors().add(new ConstructorSpec());
                    } else {
                        defaultConstructorNotPublic = true;
                    }
                } else {
                    if (e.getModifiers().contains(Modifier.PUBLIC)) {
                        nonDefaultConstructors++;

                        ConstructorSpec constructorSpec = new ConstructorSpec();
                        for (VariableElement variableElement : e.getParameters()) {
                            architect.autopath.compiler.spec.ParamSpec paramSpec = new architect.autopath.compiler.spec.ParamSpec(variableElement.getSimpleName().toString(), TypeName.get(variableElement.asType()));
                            constructorSpec.getFields().add(paramSpec);

                            if (!spec.getFields().contains(paramSpec)) {
                                spec.getFields().add(paramSpec);
                            }
                        }
                        spec.getConstructors().add(constructorSpec);
                    }
                }
            }

            if (defaultConstructorNotPublic && spec.getConstructors().isEmpty()) {
                errors.addInvalid("Path must at least have a public default constructor");
            }
            if (nonDefaultConstructors > 1) {
                errors.addInvalid("Path cannot have more than one non-default constructor");
            }

            return spec;
        }

        private ClassName buildClassName() {
            String name = extractor.getElement().getSimpleName().toString();

            // try to remove NavigationScope at the end of the name
            String newName = removeEndingName(removeEndingName(name, "Scope"), "Navigation");
            if (newName == null) {
                errors.addInvalid("Class name " + newName);
            }

            String pkg = MoreElements.getPackage(extractor.getElement()).getQualifiedName().toString();
            if (StringUtils.isBlank(pkg)) {
                errors.addInvalid("Package name " + pkg);
            }
            pkg = pkg + ".path";

            return ClassName.get(pkg, newName + "Path");
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
