package mortarnav.library;

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenState {

    private SparseArray<Parcelable> viewState;

    public void save(View view) {
        SparseArray<Parcelable> state = new SparseArray<>();
        view.saveHierarchyState(state);
        viewState = state;
    }

    public void restore(View view) {
        if (viewState != null) {
            view.restoreHierarchyState(viewState);
        }
    }
}
