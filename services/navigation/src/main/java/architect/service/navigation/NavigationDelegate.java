package architect.service.navigation;

import architect.service.Delegate;

/**
 * Created by lukasz on 23/11/15.
 */
public class NavigationDelegate extends Delegate {

    @Override
    public boolean onBackPressed() {
        if (service.<NavigationPresenter>getPresenter().onBackPressed()) {
            return true;
        }

        return service.<NavigationController>getController().pop();
    }
}
