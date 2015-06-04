package mortarnav.library;

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ViewState {

    private SparseArray<Parcelable> state;

    public void save(View view) {
        SparseArray<Parcelable> state = new SparseArray<>();
        view.saveHierarchyState(state);
        this.state = state;
    }

    public void restore(View view) {
        if (state != null) {
            view.restoreHierarchyState(state);
        }
    }
}
