package architect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class DispatchEnv {

    private Map<String, Object> map;

    public void put(String name, Object object) {
        if (map == null) {
            map = new HashMap<>();
        }

        map.put(name, object);
    }

    public <T> T get(String name) {
        if (map == null) {
            return null;
        }

        return (T) map.get(name);
    }
}
