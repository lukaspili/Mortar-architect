package mortarnav.processor;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Errors {

    private final List<Error> list = new LinkedList<>();

    public void addInvalid(Element element, String reason) {
        list.add(new Error(element, String.format("Invalid value: %s", reason)));
    }

    public void addMissing(Element element, String reason) {
        list.add(new Error(element, String.format("Missing value: %s", reason)));
    }

    public void deliver(Messager messager) {
        for (Error error : list) {
            messager.printMessage(Diagnostic.Kind.ERROR, error.text, error.element);
        }
    }

    public ElementErrors getFor(Element element) {
        return new ElementErrors(this, element);
    }

    public boolean hasErrors() {
        return !list.isEmpty();
    }

    static class Error {

        private final Element element;
        private final String text;

        public Error(Element element, String text) {
            this.element = element;
            this.text = text;
        }
    }

    public static class ElementErrors {

        private final Errors errors;
        private final Element element;

        public ElementErrors(Errors errors, Element element) {
            this.errors = errors;
            this.element = element;
        }

        public void addInvalid(String reason) {
            errors.addInvalid(element, reason);
        }

        public void addMissing(String reason) {
            errors.addMissing(element, reason);
        }
    }
}
