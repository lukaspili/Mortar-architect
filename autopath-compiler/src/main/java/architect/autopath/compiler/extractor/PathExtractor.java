package architect.autopath.compiler.extractor;

import com.google.auto.common.MoreElements;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import architect.autopath.AutoPath;
import processorworkflow.AbstractExtractor;
import processorworkflow.Errors;
import processorworkflow.ExtractorUtils;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PathExtractor extends AbstractExtractor {

    private static final String ANNOTATION_VIEW = "withView";

    private TypeMirror viewTypeMirror;
    private List<ExecutableElement> constructorElements;

    public PathExtractor(Element element, Types types, Elements elements, Errors errors) {
        super(element, types, elements, errors);
        extract();
    }

    @Override
    public void extract() {
        if (element.getKind() != ElementKind.CLASS) {
            errors.addInvalid("Annotation can be applied only on a class");
            return;
        }

        viewTypeMirror = ExtractorUtils.getValueFromAnnotation(element, AutoPath.class, ANNOTATION_VIEW);
        if (viewTypeMirror == null) {
            errors.addMissing("withView() value");
        }

        constructorElements = new ArrayList<>();
        for (Element e : element.getEnclosedElements()) {
            if (e.getKind() == ElementKind.CONSTRUCTOR) {
                constructorElements.add(MoreElements.asExecutable(e));
            }
        }
    }

    public TypeMirror getViewTypeMirror() {
        return viewTypeMirror;
    }

    public List<ExecutableElement> getConstructorElements() {
        return constructorElements;
    }
}
