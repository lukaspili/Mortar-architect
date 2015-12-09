package architect.service;

/**
 * Created by lukasz on 23/11/15.
 */
public abstract class Delegate {

    protected Service service;

    public abstract boolean onBackPressed();
}