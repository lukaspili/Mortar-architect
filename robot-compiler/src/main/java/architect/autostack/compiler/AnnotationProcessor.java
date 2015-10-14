package architect.autostack.compiler;

import com.google.auto.service.AutoService;

import java.util.LinkedList;

import javax.annotation.processing.Processor;

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
        processings.add(new ScreenProcessing(elements, types, errors, null));
        return processings;
    }

    public AnnotationProcessor() {
        Logger.init("Robot Processor", false);
    }
}
