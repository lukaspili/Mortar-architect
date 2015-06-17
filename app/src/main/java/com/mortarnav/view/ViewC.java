//package com.mortarnav.view;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.mortarnav.DaggerService;
//import com.mortarnav.R;
//import com.mortarnav.screen.ScreenC;
//
//import javax.inject.Inject;
//
//import butterknife.ButterKnife;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class ViewC extends LinearLayout {
//
//    @Inject
//    protected ScreenC.Presenter presenter;
//
//    public ViewC(Context context) {
//        super(context);
//
//        DaggerService.<ScreenC.Component>get(context).inject(this);
//
//        View view = View.inflate(context, R.layout.screen_c, this);
//        ButterKnife.inject(view);
//    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        presenter.takeView(this);
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        presenter.dropView(this);
//        super.onDetachedFromWindow();
//    }
//}
