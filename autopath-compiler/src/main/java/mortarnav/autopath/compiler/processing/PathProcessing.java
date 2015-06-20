package mortarnav.autopath.compiler.processing;

import com.google.auto.common.MoreElements;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import mortarnav.autopath.AutoPath;
import mortarnav.autopath.compiler.composer.PathComposer;
import mortarnav.autopath.compiler.extractor.PathExtractor;
import mortarnav.autopath.compiler.spec.ConstructorSpec;
import mortarnav.autopath.compiler.spec.ParamSpec;
import mortarnav.autopath.compiler.spec.PathSpec;
import mortarnav.processor.AbstractComposer;
import mortarnav.processor.AbstractProcessing;
import mortarnav.processor.Errors;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PathProcessing extends AbstractProcessing<PathSpec> {

    public PathProcessing(Elements elements, Types types, Errors errors) {
        super(elements, types, errors);
    }

    @Override
    public Class<? extends Annotation> supportedAnnotation() {
        return AutoPath.class;
    }

    @Override
    public boolean processElement(Element element, Errors.ElementErrors elementErrors) {
        PathExtractor extractor = new PathExtractor(element, types, elements, errors);
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

    private class ElementBuilder {

        private final PathExtractor extractor;
        private final Errors.ElementErrors errors;

        public ElementBuilder(PathExtractor extractor, Errors errors) {
            this.extractor = extractor;
            this.errors = errors.getFor(extractor.getElement());
        }

        private PathSpec build() {
            PathSpec spec = new PathSpec(buildClassName(), TypeName.get(extractor.getElement().asType()));
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
                            ParamSpec paramSpec = new ParamSpec(variableElement.getSimpleName().toString(), TypeName.get(variableElement.asType()));
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


//
//    private void generateSpecs(List<ComponentSpec> componentSpecs) {
//        for (ComponentSpec componentSpec : componentSpecs) {
//            TypeSpec typeSpec = misunderstoodPoet.compose(componentSpec);
//            JavaFile javaFile = JavaFile.builder(componentSpec.getClassName().packageName(), typeSpec).build();
//            write(javaFile, componentSpec.getElement());
//        }
//    }
//
//    private void write(JavaFile javaFile, Element element) {
//        try {
//            javaFile.writeTo(filer);
//        } catch (Exception e) {
//            StringWriter stackTrace = new StringWriter();
//            e.printStackTrace(new PrintWriter(stackTrace));
//            errors.add(Message.error(element, "Unable to generate class for %s. %s", javaFile.typeSpec.name, stackTrace));
//        }
//    }
}
