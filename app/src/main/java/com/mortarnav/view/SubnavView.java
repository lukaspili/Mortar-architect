//package com.mortarnav.view;
//
//import android.content.Context;
//import android.view.View;
//
//import com.mortarnav.DaggerService;
//import com.mortarnav.R;
//import com.mortarnav.nav.SubnavScope;
//import com.mortarnav.presenter.SubnavPresenter;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import mortarnav.NavigatorView;
//import mortarnav.commons.view.MvpFrameLayout;
//import mortarnav.view.HandlesBack;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class SubnavView extends MvpFrameLayout<SubnavPresenter> implements HandlesBack {
//
//    @InjectView(R.id.sub_navigator)
//    public NavigatorView navigatorView;
//
//    public SubnavView(Context context) {
//        super(context);
//
//        DaggerService.<SubnavScope.Component>get(context).inject(this);
//
//        View view = View.inflate(context, R.layout.subnav_view, this);
//        ButterKnife.inject(view);
//    }
//
//    @Override
//    public boolean onBackPressed() {
//        return presenter.backPressed();
//    }
//}
