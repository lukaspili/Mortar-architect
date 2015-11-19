package architect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by lukasz on 18/11/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class DispatcherTest {

    private static final String ENTRY_FAKESERVICE = "fakeservice";

    @Mock
    private Services services;

    @Mock
    private History history;

    @Mock
    private Services.Service service;

    @Mock
    private architect.service.Dispatcher serviceDispatcher;

    @Mock
    private Screen entryScreen;

    @Spy
    private ArrayList<History.Entry> entries;

    private Dispatcher dispatcher;
    private History.Entry entry;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dispatcher = new Dispatcher(services, history, entries);
        entry = new History.Entry(entryScreen, ENTRY_FAKESERVICE, null, null);
    }

    /**
     * Not active = nothing
     */
    @Test
    public void shouldNotDispatchWhenNotActive() {
        dispatcher.dispatch(entry);
        verify(entries, never()).add(entry);
    }

    /**
     * Empty history, backward = fail
     */
    @Test(expected = NullPointerException.class)
    public void shouldFailToDispatchEntryBackwardWithEmptyHistory() {
        when(history.existInHistory(entry)).thenReturn(false);
        when(history.getTopDispatched()).thenReturn(null);

        dispatcher.activate();
        dispatcher.dispatch(entry);
    }

    /**
     * Empty history, forward = ok
     * Enter entry service not found = fail
     */
    @Test(expected = NullPointerException.class)
    public void shouldFailToDispatchEntryWithoutRegisteredService() {
        when(history.existInHistory(entry)).thenReturn(true);

        dispatcher.activate();
        dispatcher.dispatch(entry);
    }

    @Test
    public void shouldDispatchEntryForward() {
        when(history.existInHistory(entry)).thenReturn(true);
        when(services.get(entry.service)).thenReturn(service);
        when(service.getDispatcher()).thenReturn(serviceDispatcher);

        dispatcher.activate();
        dispatcher.dispatch(entry);

        verify(serviceDispatcher, times(1)).dispatch(eq(entry), isNull(History.Entry.class), eq(true), isNull(DispatchEnv.class), any(Callback.class));
    }

}
