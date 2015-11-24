//package architect.examples.simple_app.view;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.widget.FrameLayout;
//
//import architect.service.commons.Container;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class ArchitectView extends FrameLayout implements Container {
//
//    private boolean interactionsDisabled = false;
//
//    public ArchitectView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return !interactionsDisabled && super.dispatchTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onBackPressed() {
//        return interactionsDisabled;
//    }
//
//    @Override
//    public void willBeginTransition() {
//        interactionsDisabled = true;
//    }
//
//    @Override
//    public void didEndTransition() {
//        interactionsDisabled = false;
//    }
//}
