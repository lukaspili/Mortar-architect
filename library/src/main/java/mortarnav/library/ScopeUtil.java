package mortarnav.library;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
class ScopeUtil {

    static String getScopeName(Object object) {
        return String.format("%s_%d", object.getClass().getName(), System.identityHashCode(object));
    }
}
