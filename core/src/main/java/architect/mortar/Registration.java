package architect.mortar;

import android.view.ViewGroup;

import architect.Controller;
import architect.Presenter;
import architect.Service;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface Registration<T_Service extends Service, T_Presenter extends Presenter> {

    T_Service createService(String name, Controller controller);

    T_Presenter createPresenter(ViewGroup view);
}
