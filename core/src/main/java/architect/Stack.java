package architect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukasz on 24/11/15.
 */
public class Stack {

    private final List<History.Entry> entries;

    public Stack() {
        this(new ArrayList<History.Entry>());
    }

    Stack(List<History.Entry> entries) {
        this.entries = entries;
    }

    public Stack put(String service, Screen screen) {
        entries.add(new History.Entry(screen, service, null, null));
        return this;
    }

    List<History.Entry> getEntries() {
        return entries;
    }
}
