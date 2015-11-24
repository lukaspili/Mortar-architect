package architect.service;

public class Service {
    private final Controller controller;
    private final Presenter presenter;
    private final Delegate delegate;

    public Service(Controller controller, Presenter presenter, Delegate delegate) {
        this.controller = controller;
        this.presenter = presenter;
        this.delegate = delegate;

        delegate.service = this;
    }

    public <T extends Controller> T getController() {
        return (T) controller;
    }

    public <T extends Presenter> T getPresenter() {
        return (T) presenter;
    }

    public Delegate getDelegate() {
        return delegate;
    }
}