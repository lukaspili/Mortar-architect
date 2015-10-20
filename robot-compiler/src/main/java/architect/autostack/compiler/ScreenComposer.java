package architect.autostack.compiler;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;

import architect.robot.dagger.DaggerScope;
import architect.robot.dagger.DaggerService;
import dagger.Module;
import dagger.Provides;
import mortar.MortarScope;
import processorworkflow.AbstractComposer;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenComposer extends AbstractComposer<ScreenSpec> {

    private static final ClassName SCREEN_CLS = ClassName.get("architect", "Screen");
    private static final ClassName PATH_CLS = ClassName.get("architect", "ScreenPath");
    private static final ClassName SUBSCREENSERVICE_CLS = ClassName.get("architect", "SubScreenService");
    private static final ClassName SUBSCREENSERVICE_BUILDER_CLS = ClassName.get("architect", "SubScreenService.Builder");
    private static final ClassName RECEIVESNAVRESULT_CLS = ClassName.get("architect.nav", "HandlesNavigationResult");
    private static final ClassName DAGGERSERVICE_CLS = ClassName.get(DaggerService.class);
//    private static final ClassName DAGGERSCOPE_CLS = ClassName.get(DaggerScope.class);
    private static final ClassName CONTEXT_CLS = ClassName.get("android.content", "Context");
    private static final ClassName VIEW_CLS = ClassName.get("android.view", "View");
    private static final ClassName VIEWGROUP_CLS = ClassName.get("android.view", "ViewGroup");
    private static final ClassName LAYOUTINFLATER_CLS = ClassName.get("android.view", "LayoutInflater");

    static final String SUBSCREEN_FIELD_PREFIX = "_subscreen_";
    static final String DATA_FIELD_PREFIX = "_data_";

    public ScreenComposer(List<ScreenSpec> specs) {
        super(specs);
    }

    @Override
    protected JavaFile compose(ScreenSpec spec) {
        TypeSpec typeSpec = build(spec);
        return JavaFile.builder(spec.getClassName().packageName(), typeSpec).build();
    }

    private TypeSpec build(ScreenSpec spec) {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        List<FieldSpec> fieldSpecs = new ArrayList<>();

        TypeSpec.Builder builder = TypeSpec.classBuilder(spec.getClassName().simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(Generated.class).addMember("value", "$S", architect.autostack.compiler.AnnotationProcessor.class.getName()).build())
                .addAnnotation(spec.getComponentAnnotationSpec())
                .addAnnotation(AnnotationSpec.builder(DaggerScope.class).addMember("value", "$T.class", spec.getPresenterTypeName()).build())
                .addAnnotation(AnnotationSpec.builder(Parcel.class).addMember("parcelsIndex", "false").build());

//        for (ParameterSpec parameterSpec : spec.getModuleSpec().getInternalParameters()) {
//            fieldSpecs.add(FieldSpec.builder(parameterSpec.type, parameterSpec.name)
//                    .build());
//        }

        // configureScope()
        CodeBlock.Builder configureScopeSpecCodeBuilder = CodeBlock.builder()
                .add("$T.configureScope(builder, $T.class, $T.builder()\n", DAGGERSERVICE_CLS, spec.getClassName(), spec.getDaggerComponentTypeName())
                .indent().indent()
                .add(".$L($T.<$T>getTyped(parentScope, $T.class))\n", spec.getDaggerComponentBuilderDependencyMethodName(), DAGGERSERVICE_CLS, spec.getDaggerComponentBuilderDependencyTypeName(), spec.getParentTypeName())
                .add(".module(new Module())\n")
                .add(".build());\n")
                .unindent().unindent();

        boolean constructorShouldCallInit = false;
        CodeBlock.Builder initCodeBlockBuider = CodeBlock.builder();

        // subscreens
        if (spec.getSubscreenSpecs() != null && !spec.getSubscreenSpecs().isEmpty()) {
            constructorShouldCallInit = true;
            fieldSpecs.addAll(spec.getSubscreenSpecs());

            CodeBlock.Builder subscreensBuilder = CodeBlock.builder()
                    .add("\nbuilder.withService($T.SERVICE_NAME, new $T()\n", SUBSCREENSERVICE_CLS, SUBSCREENSERVICE_BUILDER_CLS)
                    .indent();

            for (FieldSpec subscreenSpec : spec.getSubscreenSpecs()) {
                subscreensBuilder.add(".withScreen($S, $L)\n", getSubscreenName(subscreenSpec.name), subscreenSpec.name);

                // every subscreen must be initialized
                initCodeBlockBuider.addStatement("this.$L = new $T()", subscreenSpec.name, subscreenSpec.type);
            }

            subscreensBuilder.add(".build());\n").unindent();
            configureScopeSpecCodeBuilder.add(subscreensBuilder.build());
        }

        // screen data
        if (spec.getScreenDataSpecs() != null && !spec.getScreenDataSpecs().isEmpty()) {
            constructorShouldCallInit = true;
            fieldSpecs.addAll(spec.getScreenDataSpecs());

            for (FieldSpec fieldSpec : spec.getScreenDataSpecs()) {
                initCodeBlockBuider.add("this.$L = new $T();\n", fieldSpec.name, fieldSpec.type);
            }
        }

        // navigation result
        if (spec.getNavigationResultSpec() != null) {
            fieldSpecs.add(spec.getNavigationResultSpec());
            builder.addSuperinterface(ParameterizedTypeName.get(RECEIVESNAVRESULT_CLS, spec.getNavigationResultSpec().type));

            builder.addMethod(MethodSpec.methodBuilder("setNavigationResult")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(spec.getNavigationResultSpec().type, spec.getNavigationResultSpec().name)
                    .addStatement("this.$L = $L", spec.getNavigationResultSpec().name, spec.getNavigationResultSpec().name)
                    .build());
        }

        final MethodSpec parcelConstructorMethodSpec = buildParcelConstructor(spec);

        // navigation param
        if (spec.getNavigationParamFieldSpecs() != null && !spec.getNavigationParamFieldSpecs().isEmpty()) {
            fieldSpecs.addAll(spec.getNavigationParamFieldSpecs());

            for (List<ParameterSpec> parameterSpecs : spec.getNavigationParamConstructorSpecs()) {
                CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
                for (ParameterSpec ps : parameterSpecs) {
                    codeBlockBuilder.addStatement("this.$L = $L", ps.name, ps.name);
                }
                if (constructorShouldCallInit) {
                    codeBlockBuilder.addStatement("init()");
                }

                MethodSpec.Builder b = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameters(parameterSpecs)
                        .addCode(codeBlockBuilder.build());

                if (parcelConstructorMethodSpec == null && spec.getNavigationParamConstructorSpecs().size() == 1) {
                    b.addAnnotation(ParcelConstructor.class);
                }

                methodSpecs.add(b.build());
            }
        }

        if (methodSpecs.isEmpty()) {
            MethodSpec.Builder b = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC);
            if (constructorShouldCallInit) {
                b.addStatement("init()");
            }
            methodSpecs.add(b.build());
        }

        if (parcelConstructorMethodSpec != null) {
            methodSpecs.add(parcelConstructorMethodSpec);
        }

        methodSpecs.add(MethodSpec.methodBuilder("configureScope")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ClassName.get(MortarScope.Builder.class), "builder")
                .addParameter(ClassName.get(MortarScope.class), "parentScope")
                .addCode(configureScopeSpecCodeBuilder.build())
                .build());

        if (constructorShouldCallInit) {
            methodSpecs.add(MethodSpec.methodBuilder("init")
                    .addModifiers(Modifier.PRIVATE)
                    .addCode(initCodeBlockBuider.build())
                    .build());
        }

        builder.addType(buildModule(spec.getModuleSpec()))
                .addMethods(methodSpecs)
                .addFields(fieldSpecs);

//        if (spec.getScopeAnnotationSpec() != null) {
//            builder.addAnnotation(spec.getScopeAnnotationSpec());
//        }

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
            builder.addSuperinterface(SCREEN_CLS);
        }

        return builder.build();
    }

    /**
     * Build the parcel constructor if necessary, otherwise return null
     * When returning null, and if there a non default constructor, the latter should be annotated
     * with @ParcelConstructor
     */
    private MethodSpec buildParcelConstructor(ScreenSpec spec) {
        List<ParameterSpec> parcelConstructorParameterSpecs = new ArrayList<>();
        CodeBlock.Builder parcelConstructorCodeBlockBuider = CodeBlock.builder();

        if (spec.getSubscreenSpecs() != null && !spec.getSubscreenSpecs().isEmpty()) {
            for (FieldSpec subscreenSpec : spec.getSubscreenSpecs()) {
                parcelConstructorParameterSpecs.add(ParameterSpec.builder(subscreenSpec.type, subscreenSpec.name).build());
                parcelConstructorCodeBlockBuider.addStatement("this.$L = $L", subscreenSpec.name, subscreenSpec.name);
            }
        }

        if (spec.getScreenDataSpecs() != null && !spec.getScreenDataSpecs().isEmpty()) {
            for (FieldSpec dataSpec : spec.getScreenDataSpecs()) {
                parcelConstructorParameterSpecs.add(ParameterSpec.builder(dataSpec.type, dataSpec.name).build());
                parcelConstructorCodeBlockBuider.addStatement("this.$L = $L", dataSpec.name, dataSpec.name);
            }
        }

        if (spec.getNavigationParamFieldSpecs() != null && !spec.getNavigationParamFieldSpecs().isEmpty()) {
            for (FieldSpec fieldSpec : spec.getNavigationParamFieldSpecs()) {
                parcelConstructorParameterSpecs.add(ParameterSpec.builder(fieldSpec.type, fieldSpec.name).build());
                parcelConstructorCodeBlockBuider.addStatement("this.$L = $L", fieldSpec.name, fieldSpec.name);
            }
        }

        if (spec.getNavigationResultSpec() != null) {
            parcelConstructorParameterSpecs.add(ParameterSpec.builder(spec.getNavigationResultSpec().type, spec.getNavigationResultSpec().name).build());
            parcelConstructorCodeBlockBuider.addStatement("this.$L = $L", spec.getNavigationResultSpec().name, spec.getNavigationResultSpec().name);
        }

        if (parcelConstructorParameterSpecs.isEmpty()) {
            return null;
        }

        if (spec.getNavigationParamConstructorSpecs() != null &&
                spec.getNavigationParamConstructorSpecs().size() == 1 &&
                spec.getNavigationParamConstructorSpecs().get(0).size() == parcelConstructorParameterSpecs.size()) {
            return null;
        }

        return MethodSpec.constructorBuilder()
                .addAnnotation(ParcelConstructor.class)
                .addParameters(parcelConstructorParameterSpecs)
                .addCode(parcelConstructorCodeBlockBuider.build())
                .build();
    }

    private TypeSpec buildModule(ModuleSpec spec) {
        CodeBlock.Builder blockBuilder = CodeBlock.builder().add("return new $T(", spec.getPresenterTypeName());
        int i = 0;
        for (ParameterSpec parameterSpec : spec.getAllParameterSpecs()) {
            blockBuilder.add(parameterSpec.name);

            if (i++ < spec.getAllParameterSpecs().size() - 1) {
                blockBuilder.add(", ");
            }
        }
        blockBuilder.add(");\n");

        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("providesPresenter")
                .addModifiers(Modifier.PUBLIC)
                .returns(spec.getPresenterTypeName())
                .addAnnotation(Provides.class)
                .addAnnotation(AnnotationSpec.builder(DaggerScope.class).addMember("value", "$T.class", spec.getPresenterTypeName()).build())
                .addParameters(spec.getDaggerParameterSpecs())
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

    private String getSubscreenName(String name) {
        return name.substring(SUBSCREEN_FIELD_PREFIX.length());
    }
}
