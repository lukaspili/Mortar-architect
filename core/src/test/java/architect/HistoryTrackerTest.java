package architect;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by lukasz on 23/11/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class HistoryTrackerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final Screen DUMB_SCREEN = new Screen() {
        @Override
        public View createView(Context context, ViewGroup parent) {
            throw new UnsupportedOperationException();
        }
    };

    private static final Screen DUMB_SCREEN_2 = new Screen() {
        @Override
        public View createView(Context context, ViewGroup parent) {
            throw new UnsupportedOperationException();
        }
    };

    private History.Entry entry1;
    private History.Entry entry2;

    private HistoryTracker historyTracker;

    @Before
    public void setUp() {
        historyTracker = new HistoryTracker();
        entry1 = new History.Entry(DUMB_SCREEN, "service", null, null);
        entry2 = new History.Entry(DUMB_SCREEN_2, "service2", null, null);
    }

    @Test
    public void shouldIncrement() {
        assertThat(historyTracker.get(entry1)).isEqualTo(0);
        assertThat(historyTracker.get(entry2)).isEqualTo(0);

        historyTracker.increment(entry1);
        assertThat(historyTracker.get(entry1)).isEqualTo(1);
        assertThat(historyTracker.get(entry2)).isEqualTo(0);

        historyTracker.increment(entry2);
        assertThat(historyTracker.get(entry1)).isEqualTo(1);
        assertThat(historyTracker.get(entry2)).isEqualTo(1);

        historyTracker.increment(entry1);
        assertThat(historyTracker.get(entry1)).isEqualTo(2);
        assertThat(historyTracker.get(entry2)).isEqualTo(1);
    }

    @Test
    public void shouldDecrement() {
        assertThat(historyTracker.get(entry1)).isEqualTo(0);
        assertThat(historyTracker.get(entry2)).isEqualTo(0);

        historyTracker.increment(entry1);
        historyTracker.increment(entry2);
        assertThat(historyTracker.get(entry1)).isEqualTo(1);
        assertThat(historyTracker.get(entry2)).isEqualTo(1);

        historyTracker.decrement(entry1);
        assertThat(historyTracker.get(entry1)).isEqualTo(0);
        assertThat(historyTracker.get(entry2)).isEqualTo(1);

        historyTracker.decrement(entry2);
        assertThat(historyTracker.get(entry1)).isEqualTo(0);
        assertThat(historyTracker.get(entry2)).isEqualTo(0);
    }

    @Test
    public void shouldFailToDecrementNegative() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(HistoryTracker.EXCEPTION_ID_BELOW_0);

        assertThat(historyTracker.get(entry1)).isEqualTo(0);
        historyTracker.decrement(entry1);
    }
}
