package architect.autostack.compiler;

import java.util.List;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import processorworkflow.Logger;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ConstructorParameterExtractor {

    private final String name;
    private final TypeMirror type;

    public ConstructorParameterExtractor(VariableElement element, List<VariableElement> fieldElements) {
        Logger.d("Constructor param %s", element.getSimpleName());

        // kotlin fix because constructor params are named arg0, arg1, etc
        String name = element.getSimpleName().toString();
        if (name.startsWith("arg")) {
            try {
                int index = Integer.valueOf(name.substring(3));
                name = fieldElements.get(index).getSimpleName().toString();
                Logger.d("Param new name from field = %s", name);
            } catch (Exception e) {
                Logger.d("Cannot extract index from %s : %s", name, e.getMessage());
            }
        }

        this.name = name;
        this.type = element.asType();
    }

    public String getName() {
        return name;
    }

    public TypeMirror getType() {
        return type;
    }
}
