package architect;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by lukasz on 18/11/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class DispatcherTest {

    private static final String FAKESERVICE_1 = "fakeservice";
    private static final String FAKESERVICE_2 = "fakeservice2";

    private static final Screen DUMB_SCREEN = new Screen() {
        @Override
        public View createView(Context context, ViewGroup parent) {
            throw new UnsupportedOperationException();
        }
    };

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Services services;

    @Mock
    private History history;

    @Mock
    private Services.Service service;

    @Mock
    private architect.service.Dispatcher serviceDispatcher;

    @Spy
    private ArrayList<History.Entry> historyEntries;

    private Dispatcher dispatcher;
    private History.Entry entry;
    private History.Entry entry1;
    private History.Entry entry2;
    private List<History.Entry> entries;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dispatcher = new Dispatcher(services, history, historyEntries);
        entry = new History.Entry(DUMB_SCREEN, FAKESERVICE_1, null, null);

        entry1 = entry;
        entry2 = new History.Entry(DUMB_SCREEN, FAKESERVICE_2, null, null);
        entries = new ArrayList<>();
        entries.add(entry1);
        entries.add(entry2);
    }

    /**
     * Dispatch one
     * Not active = nothing
     */
    @Test
    public void shouldNotDispatchOneEntryWhenNotActive() {
        dispatcher.dispatch(entry);
        verify(historyEntries, never()).add(entry);
    }

    /**
     * Dispatch many
     * Not active = nothing
     */
    @Test
    public void shouldNotDispatchManyEntriesWhenNotActive() {
        dispatcher.dispatch(entry);
        verify(historyEntries, never()).add(entry);
    }

    /**
     * Dispatch one
     * Empty history, backward = fail
     */
    @Test
    public void shouldFailToDispatchOneEntryBackwardOnEmptyHistory() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(Dispatcher.EXCEPTION_ENTER_ENTRY_NULL);

        when(history.existInHistory(entry)).thenReturn(false);
        when(history.getTopDispatched()).thenReturn(null);

        dispatcher.activate();
        dispatcher.dispatch(entry);

        verify(services, never()).get(anyString());
    }

    /**
     * Dispatch many
     * Empty history, backward = fail
     */
    @Test
    public void shouldFailToDispatchManyEntriesBackwardOnEmptyHistory() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(Dispatcher.EXCEPTION_ENTER_ENTRY_NULL);

        when(history.existInHistory(entry2)).thenReturn(false);
        when(history.getTopDispatched()).thenReturn(null);

        dispatcher.activate();
        dispatcher.dispatch(entries);

        verify(services, never()).get(anyString());
        verify(history, never()).existInHistory(entry1);
    }

    /**
     * Dispatch one
     * Empty history, forward = ok
     * Enter entry service not found = fail
     */
    @Test
    public void shouldFailToDispatchOneEntryWhenServiceNotFound() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(String.format(Dispatcher.EXCEPTION_ENTRY_SERVICE_NULL, entry.service));

        when(history.existInHistory(entry)).thenReturn(true);
        when(history.getTopDispatched()).thenReturn(null);
        when(services.get(entry.service)).thenReturn(null);

        dispatcher.activate();
        dispatcher.dispatch(entry);

        verify(services, times(1)).get(entry.service);
    }

    /**
     * Dispatch many
     * Empty history, forward = ok
     * Enter entry service not found = fail
     */
    @Test
    public void shouldFailToDispatchManyEntriesWhenServiceNotFound() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(String.format(Dispatcher.EXCEPTION_ENTRY_SERVICE_NULL, entry2.service));

        when(history.existInHistory(entry2)).thenReturn(true);
        when(history.getTopDispatched()).thenReturn(null);
        when(services.get(entry2.service)).thenReturn(null);

        dispatcher.activate();
        dispatcher.dispatch(entries);

        verify(history, never()).existInHistory(entry1);
        verify(services, never()).get(entry1.service);
        verify(services, times(1)).get(entry2.service);
    }

    /**
     * Empty history, forward = ok
     */
    @Test
    public void shouldDispatchEntryForwardOnEmptyHistory() {
        when(history.existInHistory(entry)).thenReturn(true);
        when(history.getTopDispatched()).thenReturn(null);
        when(services.get(entry.service)).thenReturn(service);
        when(service.getDispatcher()).thenReturn(serviceDispatcher);

        dispatcher.activate();
        dispatcher.dispatch(entry);

        verify(serviceDispatcher, times(1)).dispatch(eq(entry), isNull(History.Entry.class), eq(true), isNull(DispatchEnv.class), any(Callback.class));
    }

    /**
     * Not empty history, forward = ok
     */
    @Test
    public void shouldDispatchEntryForwardOnNotEmptyHistory() {
        History.Entry exitEntry = mock(History.Entry.class);

        when(history.existInHistory(entry)).thenReturn(true);
        when(history.getTopDispatched()).thenReturn(exitEntry);
        when(services.get(entry.service)).thenReturn(service);
        when(service.getDispatcher()).thenReturn(serviceDispatcher);

        dispatcher.activate();
        dispatcher.dispatch(entry);

        verify(serviceDispatcher, times(1)).dispatch(eq(entry), eq(exitEntry), eq(true), isNull(DispatchEnv.class), any(Callback.class));
    }

    /**
     * Not empty history, backward = ok
     */
    @Test
    public void shouldDispatchEntryBackwardOnNotEmptyHistory() {
        History.Entry exitEntry = mock(History.Entry.class);

        when(history.existInHistory(exitEntry)).thenReturn(false);
        when(history.getTopDispatched()).thenReturn(entry);
        when(services.get(entry.service)).thenReturn(service);
        when(service.getDispatcher()).thenReturn(serviceDispatcher);

        dispatcher.activate();
        dispatcher.dispatch(exitEntry);

        verify(serviceDispatcher, times(1)).dispatch(eq(entry), eq(exitEntry), eq(false), isNull(DispatchEnv.class), any(Callback.class));
    }


}
