package architect.hook.mortar;

import android.content.Context;
import android.view.View;

import architect.History;
import architect.Processing;
import architect.hook.Hook;

/**
 * Created by lukasz on 26/11/15.
 */
public class MortarPresenterHook implements Hook.PresenterHook {

    @Override
    public Context getOverridedContext(View containerView, History.Entry entry, Processing processing) {
        return MortarProcessing.getScope(processing, entry).createContext(containerView.getContext());
    }
}