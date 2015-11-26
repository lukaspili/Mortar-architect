package architect;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import architect.service.Presenter;
import architect.service.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by lukasz on 18/11/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class DispatcherTest {

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
    private Service service;

    @Mock
    private Hooks hooks;

    @Mock
    private Presenter servicePresenter;

    @Spy
    private ArrayList<History.Entry> toDispatchEntries;

    private Dispatcher dispatcher;
    private History.Entry entry1;
    private History.Entry entry2;
    private List<History.Entry> entries;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dispatcher = new Dispatcher(services, history, hooks, toDispatchEntries);

        entry1 = new History.Entry(DUMB_SCREEN, "s1", null, null);
        entry2 = new History.Entry(DUMB_SCREEN, "s2", null, null);
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
        dispatcher.dispatch(entry1);
        verify(toDispatchEntries, never()).add(entry1);
    }

    /**
     * Dispatch many
     * Not active = nothing
     */
    @Test
    public void shouldNotDispatchManyEntriesWhenNotActive() {
        dispatcher.dispatch(entries);
        verify(toDispatchEntries, never()).addAll(entries);
    }

    /**
     * Dispatch one
     * Empty history, backward = fail
     */
    @Test
    public void shouldFailToDispatchOneEntryBackwardOnEmptyHistory() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(Dispatcher.EXCEPTION_DISPATCH_ENTRY_NULL);

        when(history.existInHistory(entry1)).thenReturn(false);
        when(history.getTopDispatched()).thenReturn(null);

        dispatcher.activate();
        dispatcher.dispatch(entry1);

        verify(services, never()).get(anyString());
    }

    /**
     * Dispatch many
     * Empty history, backward = fail
     */
    @Test
    public void shouldFailToDispatchManyEntriesBackwardOnEmptyHistory() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(Dispatcher.EXCEPTION_DISPATCH_ENTRY_NULL);

        when(history.existInHistory(entry2)).thenReturn(false);
        when(history.getTopDispatched()).thenReturn(null);

        dispatcher.activate();
        dispatcher.dispatch(entries);

        verify(services, never()).get(anyString());
    }

    /**
     * Dispatch one
     * Empty history, forward = ok
     * Enter entry service not found = fail
     */
    @Test
    public void shouldFailToDispatchOneEntryWhenServiceNotFound() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(String.format(Dispatcher.EXCEPTION_SERVICE_NULL, entry1.service));

        when(history.existInHistory(entry1)).thenReturn(true);
        when(history.getTopDispatched()).thenReturn(null);
        when(services.get(entry1.service)).thenReturn(null);

        dispatcher.activate();
        dispatcher.dispatch(entry1);

        verify(services, times(1)).get(entry1.service);
    }

    /**
     * Dispatch many
     * Empty history, forward = ok
     * Enter entry service not found = fail
     */
    @Test
    public void shouldFailToDispatchManyEntriesWhenServiceNotFound() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage(String.format(Dispatcher.EXCEPTION_SERVICE_NULL, entry2.service));

        when(history.existInHistory(entry2)).thenReturn(true);
        when(history.getTopDispatched()).thenReturn(null);
        when(services.get(entry2.service)).thenReturn(null);

        dispatcher.activate();
        dispatcher.dispatch(entries);

        verify(services, times(1)).get(entry2.service);
    }

    /**
     * Dispatch one
     * Empty history, forward = ok
     */
    @Test
    public void shouldDispatchEntryForwardOnEmptyHistory() {
        dispatcher.activate();
        dispatchOne(entry1, null, true);
    }

    /**
     * Dispatch many
     * Empty history, forward = ok
     */
    @Test
    public void shouldDispatchManyEntriesForwardOnEmptyHistory() {
        dispatcher.activate();
        dispatchMany(entries, null, true);
    }

    /**
     * Dispatch one
     * Not empty history, forward = ok
     */
    @Test
    public void shouldDispatchOneEntryForwardOnNotEmptyHistory() {
        dispatcher.activate();
        dispatchOne(entry1, mock(History.Entry.class), true);
    }

    /**
     * Dispatch many
     * Not empty history, forward = ok
     */
    @Test
    public void shouldDispatchManyEntriesForwardOnNotEmptyHistory() {
        dispatcher.activate();
        dispatchMany(entries, mock(History.Entry.class), true);
    }

    /**
     * Dispatch one
     * Not empty history, backward = ok
     */
    @Test
    public void shouldDispatchOneEntryBackwardOnNotEmptyHistory() {
        dispatcher.activate();
        dispatchOne(entry1, new History.Entry(DUMB_SCREEN, "s99", null, null), false);
    }

    /**
     * Dispatch many
     * Not empty history, backward = ok
     */
    @Test
    public void shouldDispatchManyEntriesBackwardOnNotEmptyHistory() {
        dispatcher.activate();
        dispatchMany(entries, new History.Entry(DUMB_SCREEN, "s99", null, null), false);
    }

    /**
     * Dispatch one
     * Empty history (for the 1st dispatch), forward = ok
     * Multiple dispatchs
     */
    @Test
    public void shouldDispatchMultipleOneEntryForwardDispatchsOnEmptyHistory() {
        History.Entry entry3 = new History.Entry(DUMB_SCREEN, "s99-1", null, null);
        History.Entry entry4 = new History.Entry(DUMB_SCREEN, "s99-2", null, null);

        InOrder inOrder = inOrder(toDispatchEntries, history, services, servicePresenter);

        dispatcher.activate();

        dispatchOne(entry1, null, true, inOrder);
        dispatchOne(entry2, entry1, true, inOrder);
        dispatchOne(entry3, entry2, true, inOrder);
        dispatchOne(entry4, entry3, true, inOrder);
    }


    /**
     * Dispatch many
     * Empty history (for the 1st dispatch), forward = ok
     * Multiple dispatchs
     */
    @Test
    public void shouldDispatchMultipleManyEntriesDispatchsOnEmptyHistory() {
        List<History.Entry> entries2 = new ArrayList<>();
        entries2.add(new History.Entry(DUMB_SCREEN, "s99-1", null, null));
        entries2.add(new History.Entry(DUMB_SCREEN, "s99-2", null, null));

        InOrder inOrder = inOrder(toDispatchEntries, history, services, servicePresenter);

        dispatcher.activate();
        dispatchMany(entries, null, true, inOrder);
        dispatchMany(entries2, entries.get(entries.size() - 1), true, inOrder);
    }

    private void dispatchOne(History.Entry entry, History.Entry top, boolean forward) {
        dispatchOne(entry, top, forward, inOrder(toDispatchEntries, history, services, servicePresenter));
    }

    private void dispatchOne(History.Entry entry, History.Entry top, boolean forward, InOrder inOrder) {
        ArgumentCaptor<Callback> callbackArgumentCaptor = ArgumentCaptor.forClass(Callback.class);

        when(history.existInHistory(entry)).thenReturn(forward);
        when(history.getTopDispatched()).thenReturn(top);
        when(services.get(forward ? entry.service : top.service)).thenReturn(service);
        when(service.getPresenter()).thenReturn(servicePresenter);

        dispatcher.dispatch(entry);
        assertTrue(entry.dispatched);
        assertThat(toDispatchEntries).isEmpty();
        inOrder.verify(toDispatchEntries, times(1)).get(0);
        inOrder.verify(services, times(1)).get(forward ? entry.service : top.service);
        inOrder.verify(servicePresenter, times(1)).present(eq(forward ? entry : top), eq(forward ? top : entry), eq(forward), isNull(Processing.class), callbackArgumentCaptor.capture());

        callbackArgumentCaptor.getValue().onComplete();
    }

    private void dispatchMany(List<History.Entry> entries, History.Entry top, boolean forward) {
        dispatchMany(entries, top, forward, inOrder(toDispatchEntries, history, services, servicePresenter));
    }

    private void dispatchMany(List<History.Entry> entries, History.Entry top, boolean forward, InOrder inOrder) {
        ArgumentCaptor<Callback> callbackArgumentCaptor = ArgumentCaptor.forClass(Callback.class);
        History.Entry entry = entries.get(entries.size() - 1);

        when(history.existInHistory(entry)).thenReturn(forward);
        when(history.getTopDispatched()).thenReturn(top);
        when(services.get(forward ? entry.service : top.service)).thenReturn(service);
        when(service.getPresenter()).thenReturn(servicePresenter);

        dispatcher.dispatch(entries);

        for (History.Entry e : entries) {
            assertTrue(e.dispatched);
        }
        assertThat(toDispatchEntries).isEmpty();

        inOrder.verify(toDispatchEntries, times(1)).get(0);
        inOrder.verify(services, times(1)).get(forward ? entry.service : top.service);
        inOrder.verify(servicePresenter, times(1)).present(eq(forward ? entry : top), eq(forward ? top : entry), eq(forward), isNull(Processing.class), callbackArgumentCaptor.capture());

        callbackArgumentCaptor.getValue().onComplete();
    }
}