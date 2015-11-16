//package architect.examples.simple_app.mvp.home;
//
//import android.content.Context;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.TextView;
//
//import com.mortarnav.R;
//
//import architect.commons.view.PresentedLinearLayout;
//import architect.robot.dagger.DaggerService;
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class HomeView extends PresentedLinearLayout<HomePresenter> {
//
//    @Bind(R.id.home_title)
//    public TextView titleTextView;
//
//    @Bind(R.id.home_subtitle)
//    public TextView subtitleTextView;
//
//    @Bind(R.id.toolbar)
//    public Toolbar toolbar;
//
//    public HomeView(Context context) {
//        super(context);
//
//        DaggerService.<HomeScreen.Component>get(context).inject(this);
//
//        View view = View.inflate(context, R.layout.home_view, this);
//        ButterKnife.bind(view);
//
//        toolbar.setTitle("Home View");
//    }
//
//    @OnClick(R.id.next_home_button)
//    void nextHomeClick() {
//        presenter.nextHomeClick();
//    }
//
//
//    @OnClick(R.id.pager_button)
//    void pagerClick() {
//        presenter.pagerClick();
//    }
//
//    @OnClick(R.id.subnav_button)
//    void subnavClick() {
//        presenter.subnavClick();
//    }
//
//    @OnClick(R.id.show_popup)
//    void showPopupClick() {
//        presenter.showPopupClick();
//    }
//
//    @OnClick(R.id.replace_new_home)
//    void replaceNewHomeClick() {
//        presenter.replaceNewHomeClick();
//    }
//
//    @OnClick(R.id.show_returns_result)
//    void showReturnsResultClick() {
//        presenter.showReturnsResultClick();
//    }
//
//    @OnClick(R.id.back_root)
//    void backRootClick() {
//        presenter.backToRootClick();
//    }
//
//    @OnClick(R.id.home_show_popup_two)
//    void showPopupTwoClick() {
//        presenter.showPopupTwoClick();
//    }
//
//    @OnClick(R.id.home_show_two_popups)
//    void showTwoPopupsClick() {
//        presenter.showTwoPopupsClick();
//    }
//
//    @OnClick(R.id.home_set_new_stack)
//    void setNewStackClick() {
//        presenter.setNewStackClick();
//    }
//
////    @Override
////    public void onViewTransition(AnimatorSet set) {
////        if (set != null) {
////            set.addListener(new AnimatorListenerAdapter() {
////
////                @Override
////                public void onAnimationStart(Animator animation) {
////                    toolbarOwner.show();
////                }
////
////                @Override
////                public void onAnimationEnd(Animator animation) {
////                    toolbarOwner.setTitle("Hello Home!");
////                }
////            });
////            Animator animator = toolbarOwner.animateShow();
////            if (animator != null) {
////                set.play(animator);
////            }
////        } else {
////            toolbarOwner.show();
////            toolbarOwner.setTitle("Hello Home!");
////        }
////    }
//}
