//package com.mortarnav.view;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.TextView;
//
//import com.mortarnav.R;
//import com.mortarnav.presenter.SubnavPagePresenter;
//import com.mortarnav.presenter.stackable.SubnavPageStackableComponent;
//
//import architect.commons.view.PresentedFrameLayout;
//import architect.robot.DaggerService;
//import autodagger.AutoInjector;
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//@AutoInjector(SubnavPagePresenter.class)
//public class SubnavPageView extends PresentedFrameLayout<SubnavPagePresenter> {
//
//    @Bind(R.id.subnav_page_title)
//    public TextView textView;
//
//    public SubnavPageView(Context context) {
//        super(context);
//
//        DaggerService.<SubnavPageStackableComponent>get(context).inject(this);
//
//        View view = View.inflate(context, R.layout.subnav_page_view, this);
//        ButterKnife.bind(view);
//    }
//
//    @OnClick
//    void onClick() {
//        presenter.next();
//    }
//}
