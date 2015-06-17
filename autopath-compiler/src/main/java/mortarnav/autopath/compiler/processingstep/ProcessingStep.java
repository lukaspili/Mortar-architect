package mortarnav.autopath.compiler.processingstep;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.lang.model.element.Element;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ProcessingStep {

     Class<? extends Annotation> annotation();

    void process(Set<? extends Element> elements);
}
