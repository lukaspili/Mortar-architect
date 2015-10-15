package architect.robot.dagger;

import javax.inject.Scope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@Scope
public @interface DaggerScope {
    Class<?> value();
}
