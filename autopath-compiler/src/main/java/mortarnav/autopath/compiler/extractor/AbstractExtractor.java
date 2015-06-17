package mortarnav.autopath.compiler.extractor;

import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import mortarnav.autopath.compiler.Errors;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class AbstractExtractor {

    protected final Element element;
    protected final Types types;
    protected final Elements elements;
    protected final Errors.ElementErrors errors;

    public AbstractExtractor(Element element, Types types, Elements elements, Errors errors) {
        this.element = element;
        this.types = types;
        this.elements = elements;
        this.errors = errors.getFor(element);
    }

    public Element getElement() {
        return element;
    }

    protected abstract void extract();
}
