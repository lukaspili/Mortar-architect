package architect.basic;

import android.content.Context;

import architect.Controller;
import architect.DispatchEnv;
import architect.History;

/**
 * Dispatcher dispatch()
 * Mortar -> Controller ->
 * dispatch() <- Mortar <-
 *
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class BasicController implements Controller {

    @Override
    public void show(History.Entry entry, DispatchEnv env, Callback callback) {

    }

//    @Override
    public void show(History.Entry entry) {



        Context context = en.scope.createContext(view.getContext());
        enterView = enterDispatchEntry.entry.path.createView(context, view);
    }
}
