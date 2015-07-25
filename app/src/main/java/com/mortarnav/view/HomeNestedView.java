//package com.mortarnav.view;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.View;
//import android.widget.TextView;
//
//import com.mortarnav.R;
//import com.mortarnav.presenter.HomeNestedPresenter;
//import com.mortarnav.presenter.stackable.HomeNestedStackable;
//import com.mortarnav.presenter.stackable.HomeNestedStackableComponent;
//
//import architect.Screen;
//import architect.commons.view.StackedLinearLayout;
//import architect.robot.DaggerService;
//import autodagger.AutoInjector;
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//@AutoInjector(HomeNestedPresenter.class)
//public class HomeNestedView extends StackedLinearLayout<HomeNestedPresenter> {
//
//    @Bind(R.id.home_sub_random)
//    public TextView randomTextView;
//
//    public HomeNestedView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public Screen getStackable() {
//        return new HomeNestedStackable();
//    }
//
//
//    @Override
//    public void initWithContext(Context context) {
//        DaggerService.<HomeNestedStackableComponent>get(context).inject(this);
//
//        View view = View.inflate(context, R.layout.home_subcontent_view, this);
//        ButterKnife.bind(view);
//    }
//
//    @OnClick
//    void click() {
//        presenter.click();
//    }
//}
