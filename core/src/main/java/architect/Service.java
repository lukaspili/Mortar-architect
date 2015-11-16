package architect;

/**
 * Architect.get(context).service("navigation").push(new MyScreen())
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class Service {

    protected final String name;
    protected final Controller controller;

    public Service(String name, Controller controller) {
        this.name = name;
        this.controller = controller;
    }

    //    protected String where;
//    protected Architect architect;


//    protected final void push(Screen screen) {
//
//    }
}
