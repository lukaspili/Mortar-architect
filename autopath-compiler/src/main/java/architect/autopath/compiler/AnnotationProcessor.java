package architect.autopath.compiler;

import com.google.auto.service.AutoService;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;

import architect.autopath.compiler.processing.PathProcessing;
import architect.processor.AbstractProcessor;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        builders.add(new PathProcessing(elements, types, errors));
    }
}
