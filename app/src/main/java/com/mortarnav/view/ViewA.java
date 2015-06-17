//package com.mortarnav.view;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.mortarnav.DaggerService;
//import com.mortarnav.R;
//import com.mortarnav.screen.ScreenA;
//
//import javax.inject.Inject;
//
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class ViewA extends LinearLayout {
//
//    @Inject
//    protected ScreenA.Presenter presenter;
//
//    public ViewA(Context context) {
//        super(context);
//
//        DaggerService.<ScreenA.Component>get(context).inject(this);
//
//        View view = View.inflate(context, R.layout.screen_a, this);
//        ButterKnife.inject(view);
//    }
//
//    @OnClick(R.id.button)
//    void buttonClick() {
//        presenter.click();
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
