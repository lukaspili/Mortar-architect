package architect.autostack.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;

import architect.autostack.DaggerService;
import dagger.Module;
import dagger.Provides;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScopeComposer extends architect.processor.AbstractComposer<ScopeSpec> {

    private static final ClassName NAVIGATIONSCOPE_CLS = ClassName.get("architect", "StackScope");
    private static final ClassName SERVICES_CLS = ClassName.get("architect", "StackScope.Services");
    private static final ClassName DAGGERSERVICE_CLS = ClassName.get(DaggerService.class);

    public ScopeComposer(List<ScopeSpec> specs) {
        super(specs);
    }

    @Override
    protected JavaFile compose(ScopeSpec spec) {
        TypeSpec typeSpec = build(spec);
        return JavaFile.builder(spec.getClassName().packageName(), typeSpec).build();
    }

    private TypeSpec build(ScopeSpec spec) {

        MethodSpec servicesMethodSpec = MethodSpec.methodBuilder("withServices")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(SERVICES_CLS)
                .addParameter(ClassName.get(MortarScope.class), "parentScope")
                .addCode(CodeBlock.builder()
                        .add("return new $T().with($T.SERVICE_NAME, $T.builder()\n", SERVICES_CLS, DAGGERSERVICE_CLS, spec.getDaggerComponentTypeName())
                        .indent()
                        .add(".$L(parentScope.<$T>getService($T.SERVICE_NAME))\n", spec.getDaggerComponentBuilderDependencyMethodName(), spec.getDaggerComponentBuilderDependencyTypeName(), DAGGERSERVICE_CLS)
                        .add(".module(new Module())\n")
                        .add(".build());\n")
                        .unindent()
                        .build())
                .build();

        List<FieldSpec> fieldSpecs = new ArrayList<>();
        for (ParameterSpec parameterSpec : spec.getModuleSpec().getPresenterArgs()) {
            fieldSpecs.add(FieldSpec.builder(parameterSpec.type, parameterSpec.name)
                    .addModifiers(Modifier.PRIVATE)
                    .build());
        }

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameters(spec.getModuleSpec().getPresenterArgs());
        for (ParameterSpec parameterSpec : spec.getModuleSpec().getPresenterArgs()) {
            constructorBuilder.addStatement("this.$L = $L", parameterSpec.name, parameterSpec.name);
        }

        TypeSpec.Builder builder = TypeSpec.classBuilder(spec.getClassName().simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(NAVIGATIONSCOPE_CLS)
                .addAnnotation(AnnotationSpec.builder(Generated.class).addMember("value", "$S", architect.autostack.compiler.AnnotationProcessor.class.getName()).build())
                .addAnnotation(spec.getComponentAnnotationSpec())
                .addType(buildModule(spec.getModuleSpec()))
                .addMethod(constructorBuilder.build())
                .addMethod(servicesMethodSpec)
                .addFields(fieldSpecs);

        if (spec.getScopeAnnotationSpec() != null) {
            builder.addAnnotation(spec.getScopeAnnotationSpec());
        }

        if (spec.getPathAnnotationSpec() != null) {
            builder.addAnnotation(spec.getPathAnnotationSpec());
        }

        return builder.build();
    }

    private TypeSpec buildModule(ModuleSpec spec) {
        CodeBlock.Builder blockBuilder = CodeBlock.builder().add("return new $T(", spec.getPresenterTypeName());
        for (ParameterSpec parameterSpec : spec.getPresenterArgs()) {
            blockBuilder.add(parameterSpec.name);
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
