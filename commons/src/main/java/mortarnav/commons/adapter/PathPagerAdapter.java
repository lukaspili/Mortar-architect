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
import mortarnav.NavigationPath;
import mortarnav.NavigationScope;
import mortarnav.NavigationScopeFactory;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class PathPagerAdapter extends PagerAdapter {

    private final Context context;
    private final List<NavigationPath> paths;
    private final Map<NavigationPath, NavigationScope> scopes;

    public PathPagerAdapter(Context context, NavigationPath... paths) {
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
        NavigationPath path = paths.get(position);
        NavigationScope scope = scopes.get(path);
        if (scope == null) {
            scope = path.createScope();
            scopes.put(path, scope);
        }

        Context pageContext = NavigationScopeFactory.createContext(context, scope, String.valueOf(position));
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
