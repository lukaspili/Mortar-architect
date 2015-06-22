package mortarnav.autoscope.compiler;

import com.google.auto.service.AutoService;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

import mortarnav.processor.AbstractProcessor;
import mortarnav.processor.Logger;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        builders.add(new ScopeProcessing(elements, types, errors));
    }
}
