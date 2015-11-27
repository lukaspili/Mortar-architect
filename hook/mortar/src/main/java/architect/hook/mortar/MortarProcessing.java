package architect.hook.mortar;

import android.support.v4.util.SimpleArrayMap;

import java.util.List;

import architect.History;
import architect.Processing;
import mortar.MortarScope;

/**
 * Created by lukasz on 26/11/15.
 */
public class MortarProcessing {

    private static final String SCOPES_MAP = MortarProcessing.class.getName() + "_scopes_map";
    private static final String SINGLE_SCOPE = MortarProcessing.class.getName() + "_single_scope";

    public static void putSingleScope(Processing processing, MortarScope scope) {
        processing.put(SINGLE_SCOPE, scope);
    }

    public static void putScope(Processing processing, History.Entry entry, MortarScope scope) {
        SimpleArrayMap<History.Entry, MortarScope> map;
        if (processing.contains(SCOPES_MAP)) {
            map = processing.get(SCOPES_MAP);
        } else {
            map = new SimpleArrayMap<>();
            processing.put(SCOPES_MAP, map);
        }

        map.put(entry, scope);
    }

    public static MortarScope getScope(Processing processing, History.Entry entry) {
        MortarScope scope = processing.get(SINGLE_SCOPE);
        if (scope != null) {
            return scope;
        }

        if (processing.contains(SCOPES_MAP)) {
            scope = processing.<SimpleArrayMap<History.Entry, MortarScope>>get(SCOPES_MAP).get(entry);
            if (scope != null) {
                return scope;
            }
        }

        throw new IllegalStateException("Mortar scope not found for entry: " + entry);
    }

    public static void clear(Processing processing) {
        processing.remove(SINGLE_SCOPE);
        processing.remove(SCOPES_MAP);
    }
}
