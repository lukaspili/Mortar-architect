package architect;

import architect.service.*;

/**
 * Created by lukasz on 18/11/15.
 */
class Services {

    private final SimpleArrayMap<String, Service> registrations;

    public Services() {
        this(new SimpleArrayMap<String, Service>());
    }

    public Services(SimpleArrayMap<String, Service> registrations) {
        this.registrations = registrations;
    }

    void register(String name, Controller controller, architect.service.Dispatcher dispatcher) {
        registrations.put(name, new Service(controller, dispatcher));
    }

    Service get(String name) {
        return registrations.get(name);
    }

    public static class Service {
        private final Controller controller;
        private final architect.service.Dispatcher dispatcher;

        public Service(Controller controller, architect.service.Dispatcher dispatcher) {
            this.controller = controller;
            this.dispatcher = dispatcher;
        }

        <T extends Controller> T getController() {
            return (T) controller;
        }

        <T extends architect.service.Dispatcher> T getDispatcher() {
            return (T) dispatcher;
        }
    }
}
