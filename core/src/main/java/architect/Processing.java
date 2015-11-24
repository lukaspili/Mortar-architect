package architect;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Processing {

    private Map<String, Object> data;

    public void put(String name, Object object) {
        if (data == null) {
            data = new HashMap<>();
        }

        data.put(name, object);
    }

    public <T> T get(String name) {
        if (data == null) {
            return null;
        }

        return (T) data.get(name);
    }
}
