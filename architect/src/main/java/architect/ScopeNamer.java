package architect;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Stack scope names policy
 * Tracks and provides unique names for stack scopes
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScopeNamer implements Parcelable {

    private final Map<String, Integer> ids;

    public ScopeNamer() {
        ids = new HashMap<>();
    }

    private ScopeNamer(Parcel parcel) {
        ids = (HashMap<String, Integer>) parcel.readSerializable();
    }

    public String getName(Object scope) {
        String name = scope.getClass().getName();
        int id;
        if (ids.containsKey(name)) {
            id = ids.get(name);
        } else {
            id = 0;
        }

        ids.put(name, ++id);
        return String.format("%s_%d", name, id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable((HashMap<String, Integer>) ids);
    }

    public static final Parcelable.Creator<ScopeNamer> CREATOR = new Parcelable.Creator<ScopeNamer>() {
        @Override
        public ScopeNamer createFromParcel(Parcel source) {
            return new ScopeNamer(source);
        }

        @Override
        public ScopeNamer[] newArray(int size) {
            return new ScopeNamer[size];
        }
    };
}
