package architect.autopath.compiler;

import com.google.auto.service.AutoService;

import java.util.LinkedList;

import javax.annotation.processing.Processor;

import architect.autopath.compiler.processing.PathProcessing;
import processorworkflow.AbstractProcessing;
import processorworkflow.AbstractProcessor;
import processorworkflow.Logger;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor<Void> {

    @Override
    protected Void processingState() {
        return null;
    }

    @Override
    protected LinkedList<AbstractProcessing> processings() {
        LinkedList<AbstractProcessing> processings = new LinkedList<>();
        processings.add(new PathProcessing(elements, types, errors, null));
        return processings;
    }

    public AnnotationProcessor() {
        Logger.init("AutoPath Processor", false);
    }
}
