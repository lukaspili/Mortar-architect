package architect.commons.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import architect.StackFactory;
import architect.StackPath;
import architect.StackScope;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class StackPagerAdapter extends PagerAdapter {

    private final Context context;
    private final List<StackPath> paths;
    private final SparseArray<StackScope> scopes;

    public StackPagerAdapter(Context context, StackPath... paths) {
        this(context, Arrays.asList(paths));
    }

    public StackPagerAdapter(Context context, List<StackPath> paths) {
        this.context = context;
        this.paths = paths;
        scopes = new SparseArray<>(paths.size());
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        StackPath path = paths.get(position);
        StackScope scope = scopes.get(position);
        if (scope == null) {
            scope = path.createScope();
            scopes.put(position, scope);
        }

        Context pageContext = StackFactory.createContext(context, scope, String.valueOf(position));
        View newChild = path.createView(pageContext);
        container.addView(newChild);
        return newChild;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = ((View) object);
        MortarScope scope = MortarScope.getScope(view.getContext());
        container.removeView(view);
        scope.destroy();
        scopes.remove(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
