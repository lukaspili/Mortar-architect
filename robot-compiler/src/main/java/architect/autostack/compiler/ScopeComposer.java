package architect.autostack.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;

import architect.robot.DaggerService;
import dagger.Module;
import dagger.Provides;
import mortar.MortarScope;
import processorworkflow.AbstractComposer;
import processorworkflow.Logger;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScopeComposer extends AbstractComposer<ScopeSpec> {

    private static final ClassName STACKABLE_CLS = ClassName.get("architect", "Stackable");
    private static final ClassName PATH_CLS = ClassName.get("architect", "StackablePath");
    private static final ClassName DAGGERSERVICE_CLS = ClassName.get(DaggerService.class);
    private static final ClassName CONTEXT_CLS = ClassName.get("android.content", "Context");
    private static final ClassName VIEW_CLS = ClassName.get("android.view", "View");
    private static final ClassName VIEWGROUP_CLS = ClassName.get("android.view", "ViewGroup");
    private static final ClassName LAYOUTINFLATER_CLS = ClassName.get("android.view", "LayoutInflater");

    public ScopeComposer(List<ScopeSpec> specs) {
        super(specs);
    }

    @Override
    protected JavaFile compose(ScopeSpec spec) {
        TypeSpec typeSpec = build(spec);
        return JavaFile.builder(spec.getClassName().packageName(), typeSpec).build();
    }

    private TypeSpec build(ScopeSpec spec) {
        MethodSpec configureScopeSpec = MethodSpec.methodBuilder("configureScope")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ClassName.get(MortarScope.Builder.class), "builder")
                .addParameter(ClassName.get(MortarScope.class), "parentScope")
                .addCode(CodeBlock.builder()
                        .add("builder.withService($T.SERVICE_NAME, $T.builder()\n", DAGGERSERVICE_CLS, spec.getDaggerComponentTypeName())
                        .indent()
                        .add(".$L(parentScope.<$T>getService($T.SERVICE_NAME))\n", spec.getDaggerComponentBuilderDependencyMethodName(), spec.getDaggerComponentBuilderDependencyTypeName(), DAGGERSERVICE_CLS)
                        .add(".module(new Module())\n")
                        .add(".build());\n")
                        .unindent()
                        .build())
                .build();


        List<FieldSpec> fieldSpecs = new ArrayList<>();
        for (ParameterSpec parameterSpec : spec.getModuleSpec().getInternalParameters()) {
            fieldSpecs.add(FieldSpec.builder(parameterSpec.type, parameterSpec.name)
                    .build());
        }

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(ParcelConstructor.class).build())
                .addParameters(spec.getModuleSpec().getInternalParameters());
        for (ParameterSpec parameterSpec : spec.getModuleSpec().getInternalParameters()) {
            constructorBuilder.addStatement("this.$L = $L", parameterSpec.name, parameterSpec.name);
        }

        TypeSpec.Builder builder = TypeSpec.classBuilder(spec.getClassName().simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(Generated.class).addMember("value", "$S", architect.autostack.compiler.AnnotationProcessor.class.getName()).build())
                .addAnnotation(spec.getComponentAnnotationSpec())
                .addAnnotation(AnnotationSpec.builder(Parcel.class).addMember("parcelsIndex", "false").build())
                .addType(buildModule(spec.getModuleSpec()))
                .addMethod(constructorBuilder.build())
                .addMethod(configureScopeSpec)
                .addFields(fieldSpecs);

        if (spec.getScopeAnnotationSpec() != null) {
            builder.addAnnotation(spec.getScopeAnnotationSpec());
        }

        if (spec.getPathViewTypeName() != null || spec.getPathLayout() != null) {
            builder.addSuperinterface(PATH_CLS);
            MethodSpec.Builder createViewSpecBuilder = MethodSpec.methodBuilder("createView")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(spec.getPathViewTypeName() != null ? spec.getPathViewTypeName() : VIEW_CLS)
                    .addAnnotation(Override.class)
                    .addParameter(CONTEXT_CLS, "context")
                    .addParameter(VIEWGROUP_CLS, "parent");

            if (spec.getPathViewTypeName() != null) {
                createViewSpecBuilder.addStatement("return new $T(context)", spec.getPathViewTypeName());
            } else {
                createViewSpecBuilder.addStatement("return $T.from(context).inflate($L, parent, false)", LAYOUTINFLATER_CLS, spec.getPathLayout());
            }
            builder.addMethod(createViewSpecBuilder.build());
        } else {
            builder.addSuperinterface(STACKABLE_CLS);
        }

        return builder.build();
    }

    private TypeSpec buildModule(ModuleSpec spec) {
        CodeBlock.Builder blockBuilder = CodeBlock.builder().add("return new $T(", spec.getPresenterTypeName());
        int i = 0;
        for (ParameterSpec parameterSpec : spec.getPresenterArgs()) {
            blockBuilder.add(parameterSpec.name);

            if (i++ < spec.getPresenterArgs().size() - 1) {
                blockBuilder.add(", ");
            }
        }
        blockBuilder.add(");\n");

        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("providesPresenter")
                .addModifiers(Modifier.PUBLIC)
                .returns(spec.getPresenterTypeName())
                .addAnnotation(Provides.class)
                .addParameters(spec.getProvideParameters())
                .addCode(blockBuilder.build());

        if (spec.getScopeAnnotationSpec() != null) {
            methodSpecBuilder.addAnnotation(spec.getScopeAnnotationSpec());
        }

        return TypeSpec.classBuilder(spec.getClassName().simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Module.class)
                .addMethod(methodSpecBuilder.build())
                .build();
    }
}
