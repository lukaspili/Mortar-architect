package architect.adapter;

import architect.History;
import architect.Processing;

/**
 * Created by lukasz on 24/11/15.
 */
public interface DispatcherAdapter {

    void setUpDispatch(History.Entry enterEntry, History.Entry exitEntry, Processing processing);

    void tearDownDispatch(History.Entry enterEntry, History.Entry exitEntry, Processing processing);
}
