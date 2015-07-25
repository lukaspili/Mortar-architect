//package com.mortarnav.view;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.View;
//
//import com.mortarnav.R;
//import com.mortarnav.presenter.HomeDoubleNestedPresenter;
//import com.mortarnav.presenter.stackable.HomeDoubleNestedStackable;
//import com.mortarnav.presenter.stackable.HomeDoubleNestedStackableComponent;
//
//import architect.Screen;
//import architect.commons.view.StackedLinearLayout;
//import architect.robot.DaggerService;
//import autodagger.AutoInjector;
//import butterknife.ButterKnife;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//@AutoInjector(HomeDoubleNestedPresenter.class)
//public class HomeDoubleNestedView extends StackedLinearLayout<HomeDoubleNestedPresenter> {
//
//
//    public HomeDoubleNestedView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public Screen getStackable() {
//        return new HomeDoubleNestedStackable();
//    }
//
//    @Override
//    public void initWithContext(Context context) {
//        DaggerService.<HomeDoubleNestedStackableComponent>get(context).inject(this);
//
//        View view = View.inflate(context, R.layout.home_double_subcontent_view, this);
//        ButterKnife.bind(view);
//    }
//}
