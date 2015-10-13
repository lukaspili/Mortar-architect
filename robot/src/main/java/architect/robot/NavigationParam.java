package architect.robot;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public @interface NavigationParam {

    int[] group() default 0;
}
