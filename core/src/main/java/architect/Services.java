package architect;

import android.support.v4.util.SimpleArrayMap;

import architect.service.*;

/**
 * Created by lukasz on 18/11/15.
 */
class Services {

    private final SimpleArrayMap<String, Service> services;

    Services() {
        this(new SimpleArrayMap<String, Service>());
    }

    Services(SimpleArrayMap<String, Service> services) {
        this.services = services;
    }

    void register(String name, Controller controller, Presenter presenter, Delegate delegate) {
        services.put(name, new Service(controller, presenter, delegate));
    }

    Service get(String name) {
        return services.get(name);
    }
}
