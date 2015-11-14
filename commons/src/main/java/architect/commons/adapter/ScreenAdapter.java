package architect.commons.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import architect.Screen;
import architect.MortarFactory;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenAdapter extends PagerAdapter {

    private final Context context;
    private final List<Screen> paths;

    public ScreenAdapter(Context context, Screen... paths) {
        this(context, Arrays.asList(paths));
    }

    public ScreenAdapter(Context context, List<Screen> paths) {
        this.context = context;
        this.paths = paths;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Screen path = paths.get(position);

        Context pageContext = MortarFactory.createContext(context, path, String.valueOf(position));
        View newChild = path.createView(pageContext, container);
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
