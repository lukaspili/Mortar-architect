package architect.service.show;

import android.view.View;

import architect.Callback;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface ShowTransition {

    void show(View view, Callback callback);

    void hide(View view, Callback callback);
}
