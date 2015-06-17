package mortarnav.autopath.compiler.spec;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ConstructorSpec {

    private final List<FieldSpec> fields;

    public ConstructorSpec() {
        fields = new LinkedList<>();
    }

    public List<FieldSpec> getFields() {
        return fields;
    }

    public boolean isDefault() {
        return fields.isEmpty();
    }
}
