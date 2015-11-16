package architect.autopath.compiler.composer;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;

import architect.autopath.compiler.spec.ConstructorSpec;
import architect.autopath.compiler.spec.ParamSpec;
import architect.autopath.compiler.spec.PathSpec;
import processorworkflow.AbstractComposer;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PathComposer extends AbstractComposer<PathSpec> {

    private static final ClassName NAVIGATIONPATH_CLS = ClassName.get("architect", "StackPath");
    private static final ClassName PARCEL_CLS = ClassName.get("android.os", "Parcel");
    private static final ClassName PARCEL_CREATOR_CLS = ClassName.get("android.os.Parcelable", "Creator");
    private static final ClassName CONTEXT_CLS = ClassName.get("android.content", "Context");

    public PathComposer(List<architect.autopath.compiler.spec.PathSpec> specs) {
        super(specs);
    }

    @Override
    protected JavaFile compose(PathSpec spec) {
        TypeSpec typeSpec = build(spec);
        return JavaFile.builder(spec.getClassName().packageName(), typeSpec).build();
    }

    private TypeSpec build(PathSpec spec) {
        StringBuilder constructorParamsStringBuilder = new StringBuilder();
        List<FieldSpec> fieldSpecs = new ArrayList<>();
        for (ParamSpec paramSpec : spec.getFields()) {
            fieldSpecs.add(FieldSpec.builder(paramSpec.getTypeName(), paramSpec.getName()).build());
            constructorParamsStringBuilder.append(paramSpec.getName()).append(", ");
        }
        if (constructorParamsStringBuilder.length() > 0) {
            constructorParamsStringBuilder.delete(constructorParamsStringBuilder.length() - 2, constructorParamsStringBuilder.length());
        }
        String constructorParamsString = constructorParamsStringBuilder.toString();

        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (ConstructorSpec constructorSpec : spec.getConstructors()) {
            MethodSpec.Builder builder = MethodSpec.constructorBuilder();
            builder.addModifiers(Modifier.PUBLIC);
            for (architect.autopath.compiler.spec.ParamSpec paramSpec : constructorSpec.getFields()) {
                builder.addParameter(paramSpec.getTypeName(), paramSpec.getName());
                builder.addStatement("this.$L = $L", paramSpec.getName(), paramSpec.getName());
            }
            methodSpecs.add(builder.build());
        }

        MethodSpec parcelConstructorMethodSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(PARCEL_CLS, "parcel")
                .addStatement("super(parcel)")
                .build();
        methodSpecs.add(parcelConstructorMethodSpec);

        MethodSpec withScopeSpec = MethodSpec.methodBuilder("withScope")
                .addModifiers(Modifier.PUBLIC)
                .returns(spec.getTargetTypeName())
                .addAnnotation(Override.class)
                .addStatement("return new $T($L)", spec.getTargetTypeName(), constructorParamsString)
                .build();
        methodSpecs.add(withScopeSpec);

        MethodSpec withViewSpec = MethodSpec.methodBuilder("withView")
                .addModifiers(Modifier.PUBLIC)
                .returns(spec.getViewTypeName())
                .addAnnotation(Override.class)
                .addParameter(CONTEXT_CLS, "context")
                .addStatement("return new $T(context)", spec.getViewTypeName())
                .build();
        methodSpecs.add(withViewSpec);

        ClassName parcelablePleaseClassName = ClassName.get(spec.getClassName().packageName(), spec.getClassName().simpleName() + "ParcelablePlease");

        MethodSpec readParcelSpec = MethodSpec.methodBuilder("readParcel")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .addParameter(PARCEL_CLS, "parcel")
                .addStatement("$T.readFromParcel(this, parcel)", parcelablePleaseClassName)
                .build();
        methodSpecs.add(readParcelSpec);

        MethodSpec writeParcelSpec = MethodSpec.methodBuilder("writeParcel")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .addParameter(PARCEL_CLS, "parcel")
                .addStatement("$T.writeToParcel(this, parcel, 0)", parcelablePleaseClassName)
                .build();
        methodSpecs.add(writeParcelSpec);

        MethodSpec creatorCreateFromParcelSpec = MethodSpec.methodBuilder("createFromParcel")
                .addModifiers(Modifier.PUBLIC)
                .returns(spec.getClassName())
                .addAnnotation(Override.class)
                .addParameter(PARCEL_CLS, "in")
                .addStatement("return new $T(in)", spec.getClassName())
                .build();

        MethodSpec creatorNewArraySpec = MethodSpec.methodBuilder("newArray")
                .addModifiers(Modifier.PUBLIC)
                .returns(ArrayTypeName.of(spec.getClassName()))
                .addAnnotation(Override.class)
                .addParameter(TypeName.INT, "size")
                .addStatement("return new $T[size]", spec.getClassName())
                .build();

        TypeName creatorTypeName = ParameterizedTypeName.get(PARCEL_CREATOR_CLS, spec.getClassName());
        TypeSpec creatorTypeSpec = TypeSpec.anonymousClassBuilder("")
                .superclass(creatorTypeName)
                .addMethod(creatorCreateFromParcelSpec)
                .addMethod(creatorNewArraySpec)
                .build();

        FieldSpec creatorSpec = FieldSpec.builder(creatorTypeName, "CREATOR")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$L", creatorTypeSpec)
                .build();
        fieldSpecs.add(creatorSpec);

        TypeSpec typeSpec = TypeSpec.classBuilder(spec.getClassName().simpleName())
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(NAVIGATIONPATH_CLS, spec.getTargetTypeName()))
                .addAnnotation(AnnotationSpec.builder(Generated.class).addMember("value", "$S", architect.autopath.compiler.AnnotationProcessor.class.getName()).build())
                .addAnnotation(AnnotationSpec.builder(ParcelablePlease.class).build())
                .addFields(fieldSpecs)
                .addMethods(methodSpecs)
                .build();

        return typeSpec;
    }
}
