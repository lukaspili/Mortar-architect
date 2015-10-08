package architect.autostack.compiler;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;

import architect.robot.ContainsSubscreen;
import processorworkflow.ExtractorUtils;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class SubscreensExtractor {

    private static final String TYPE = "type";
    private static final String NAME = "name";

    private String name;
    private TypeMirror typeMirror;

    public SubscreensExtractor(AnnotationValue value) {
        AnnotationMirror annotationMirror = (AnnotationMirror) value.getValue();
        typeMirror = ExtractorUtils.getValueFromAnnotation(annotationMirror, ContainsSubscreen.class, TYPE);
        name = ExtractorUtils.getValueFromAnnotation(annotationMirror, ContainsSubscreen.class, NAME);
    }

    public String getName() {
        return name;
    }

    public TypeMirror getTypeMirror() {
        return typeMirror;
    }
}
