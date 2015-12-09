package architect;

import android.support.v4.util.SimpleArrayMap;

import java.util.ArrayList;
import java.util.List;

import architect.service.Controller;
import architect.service.Delegate;
import architect.service.Presenter;
import architect.service.Service;

/**
 * Created by lukasz on 18/11/15.
 */
class Services {

    private final History history;
    private final SimpleArrayMap<String, Service> services;

    Services(History history) {
        this(history, new SimpleArrayMap<String, Service>());
    }

    Services(History history, SimpleArrayMap<String, Service> services) {
        this.history = history;
        this.services = services;
    }

    void register(String name, Controller controller, Presenter presenter, Delegate delegate) {
        services.put(name, new Service(controller, presenter, delegate));
    }

    Service get(String name) {
        return services.get(name);
    }

    SimpleArrayMap<String, List<History.Entry>> findServicesInHistory() {
        final List<History.Entry> entries = history.entries();
        final SimpleArrayMap<String, List<History.Entry>> servicesEntries = new SimpleArrayMap<>();

        History.Entry entry;
        List<History.Entry> list;
        for (int i = 0; i < entries.size(); i++) {
            entry = entries.get(i);
            if (servicesEntries.containsKey(entry.service)) {
                list = servicesEntries.get(entry.service);
            } else {
                list = new ArrayList<>();
                servicesEntries.put(entry.service, list);
            }
            list.add(entry);
        }

        return servicesEntries;
    }

    boolean handlesOnBackPressed() {
        final List<History.Entry> entries = history.entries();
        for (int i = entries.size() - 1; i >= 0; i--) {
            if (services.get(entries.get(i).service).getDelegate().onBackPressed()) {
                return true;
            }
        }

        return false;
    }
}
