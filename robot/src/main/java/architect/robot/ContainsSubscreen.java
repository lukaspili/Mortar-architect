package architect.robot;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public @interface ContainsSubscreen {

    Class<?> type();

    String name();
}
