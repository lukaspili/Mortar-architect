package architect;

import android.support.v4.util.SimpleArrayMap;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Processing {

    private SimpleArrayMap<String, Object> data;

    public void put(String name, Object object) {
        if (data == null) {
            data = new SimpleArrayMap<>();
        }

        data.put(name, object);
    }

    public <T> T get(String name) {
        if (data == null) {
            return null;
        }

        return (T) data.get(name);
    }

    public boolean contains(Object name) {
        return data.containsKey(name);
    }

    public void remove(String name) {
        data.remove(name);
    }
}
