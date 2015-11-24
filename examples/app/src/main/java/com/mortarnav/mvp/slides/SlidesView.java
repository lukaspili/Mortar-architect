package architect.examples.mortar_app.mvp.slides;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mortarnav.R;
import architect.examples.mortar_app.mvp.slides.screen.SlidesScreenComponent;

import architect.Screen;
import architect.commons.adapter.ScreenAdapter;
import architect.commons.view.PresentedFrameLayout;
import architect.robot.dagger.DaggerService;
import autodagger.AutoInjector;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(SlidesPresenter.class)
public class SlidesView extends PresentedFrameLayout<SlidesPresenter> {

    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    @Bind(R.id.pager)
    public ViewPager viewPager;

    public SlidesView(Context context) {
        super(context);
        DaggerService.<SlidesScreenComponent>get(context).inject(this);

        View view = View.inflate(context, R.layout.slides_view, this);
        ButterKnife.bind(view);

        toolbar.setTitle("Slide View");
    }

    public void show(Screen... screens) {
        ScreenAdapter adapter = new ScreenAdapter(getContext(), screens);
        viewPager.setAdapter(adapter);
    }


}
