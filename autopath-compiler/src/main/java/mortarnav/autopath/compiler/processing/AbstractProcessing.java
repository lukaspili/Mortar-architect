package mortarnav.autopath.compiler.processing;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import mortarnav.autopath.compiler.Errors;
import mortarnav.autopath.compiler.composer.AbstractComposer;
import mortarnav.autopath.compiler.spec.AbstractSpec;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class AbstractProcessing<T extends AbstractSpec> {

    protected final Elements elements;
    protected final Types types;
    protected final Errors errors;
    protected final List<T> specs;

    public AbstractProcessing(Elements elements, Types types, Errors errors) {
        this.elements = elements;
        this.types = types;
        this.errors = errors;
        specs = new ArrayList<>();
    }

    public abstract Class<? extends Annotation> supportedAnnotation();

    public void process(Set<? extends Element> annotationElements) {
        for (Element e : annotationElements) {
            boolean success = processElement(e, errors.getFor(e));
            if (!success) {
                return;
            }
        }
    }

    public abstract boolean processElement(Element element, Errors.ElementErrors elementErrors);

    public abstract AbstractComposer<T> createComposer();
}
