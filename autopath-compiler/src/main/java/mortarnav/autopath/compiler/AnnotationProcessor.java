package mortarnav.autopath.compiler;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.JavaFile;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import mortarnav.autopath.compiler.composer.AbstractComposer;
import mortarnav.autopath.compiler.processing.AbstractProcessing;
import mortarnav.autopath.compiler.processing.PathProcessing;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private Elements elements;
    private Types types;
    private Filer filer;
    private Errors errors;
    private List<AbstractProcessing> builders;

    private boolean stop;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        elements = processingEnv.getElementUtils();
        types = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        errors = new Errors();

        builders = new LinkedList<>();
        builders.add(new PathProcessing(elements, types, errors));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (stop) return false;

        List<AbstractComposer> composers = new ArrayList<>();

        for (AbstractProcessing processingStep : builders) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(processingStep.supportedAnnotation());
            processingStep.process(elements);

            if (isInvalid()) return false;
            composers.add(processingStep.createComposer());
        }

        List<JavaFile> javaFiles = new ArrayList<>();
        for (AbstractComposer composer : composers) {
            javaFiles.addAll(composer.compose());
        }

        for (JavaFile javaFile : javaFiles) {
            try {
                javaFile.writeTo(filer);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));
            }
        }

        return false;
    }

    private boolean isInvalid() {
        if (errors.hasErrors()) {
            errors.deliver(processingEnv.getMessager());
            stop = true;
            return true;
        }

        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        for (AbstractProcessing step : builders) {
            builder.add(step.supportedAnnotation().getName());
        }
        return builder.build();
    }
}
