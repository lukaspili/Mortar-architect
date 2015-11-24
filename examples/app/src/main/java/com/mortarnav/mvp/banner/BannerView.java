package architect.examples.mortar_app.mvp.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.mortarnav.R;
import architect.examples.mortar_app.mvp.banner.screen.BannerScreenComponent;

import architect.MortarFactory;
import architect.SubScreenService;
import architect.commons.view.PresentedLinearLayout;
import architect.robot.dagger.DaggerService;
import autodagger.AutoInjector;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoInjector(BannerPresenter.class)
public class BannerView extends PresentedLinearLayout<BannerPresenter> {

    public BannerView(Context parentContext, AttributeSet attrs) {
        super(parentContext, attrs);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BannerView, 0, 0);
        String screen;
        try {
            screen = a.getString(R.styleable.BannerView_banner_screen_name);
        } finally {
            a.recycle();
        }

        Context screenContext = MortarFactory.createContext(context, SubScreenService.get(context, screen), screen);

        DaggerService.<BannerScreenComponent>get(screenContext).inject(this);
        View view = View.inflate(screenContext, R.layout.banner_view, this);
        ButterKnife.bind(view);
    }

    @OnClick
    void click() {
        presenter.click();
    }


}
