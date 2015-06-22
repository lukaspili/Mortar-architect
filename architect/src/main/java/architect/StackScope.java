package architect;

import java.util.LinkedHashMap;
import java.util.Map;

import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface StackScope {

    Services withServices(MortarScope parentScope);

    class Services {

        final Map<String, Object> services = new LinkedHashMap<>();

        public Services() {
        }

        public Services with(String service, Object object) {
            services.put(service, object);
            return this;
        }
    }
}
