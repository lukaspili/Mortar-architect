package architect.autopath.compiler.spec;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ConstructorSpec {

    private final List<ParamSpec> fields;

    public ConstructorSpec() {
        fields = new LinkedList<>();
    }

    public List<ParamSpec> getFields() {
        return fields;
    }

    public boolean isDefault() {
        return fields.isEmpty();
    }
}
