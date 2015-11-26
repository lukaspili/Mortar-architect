package architect.service;

/**
 * Created by lukasz on 23/11/15.
 */
public abstract class Delegate {

    protected Service service;

    public boolean onBackPressed() {
        return service.getController().pop();
    }
}
