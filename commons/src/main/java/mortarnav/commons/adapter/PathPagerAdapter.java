package mortarnav.commons.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mortar.MortarScope;
import mortarnav.StackPath;
import mortarnav.StackScope;
import mortarnav.StackFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PathPagerAdapter extends PagerAdapter {

    private final Context context;
    private final List<StackPath> paths;
    private final Map<StackPath, StackScope> scopes;

    public PathPagerAdapter(Context context, StackPath... paths) {
        this.context = context;
        this.paths = Arrays.asList(paths);
        scopes = new HashMap<>();
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        StackPath path = paths.get(position);
        StackScope scope = scopes.get(path);
        if (scope == null) {
            scope = path.createScope();
            scopes.put(path, scope);
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
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
