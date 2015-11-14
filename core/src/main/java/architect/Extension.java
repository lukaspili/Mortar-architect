package architect;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Extension {

    void setUp(History.Entry entry, DispatchEnv env);

    void tearDown(History.Entry entry, DispatchEnv env);
}
