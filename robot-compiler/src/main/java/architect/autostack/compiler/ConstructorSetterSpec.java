package architect.autostack.compiler;

import com.squareup.javapoet.ParameterSpec;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ConstructorSetterSpec {

    private final ParameterSpec parameterSpec;
    private final String fieldName;

    public ConstructorSetterSpec(ParameterSpec parameterSpec, String fieldName) {
        this.parameterSpec = parameterSpec;
        this.fieldName = fieldName;
    }

    public ParameterSpec getParameterSpec() {
        return parameterSpec;
    }

    public String getFieldName() {
        return fieldName;
    }
}
