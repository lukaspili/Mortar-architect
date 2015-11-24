package architect.examples.mortar_app.deps;

import com.mortarnav.App;

import javax.inject.Inject;

import architect.robot.dagger.DaggerScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@DaggerScope(App.class)
public class UserManager {

    @Inject
    public UserManager() {
    }
}
