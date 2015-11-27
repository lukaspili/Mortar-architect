//package architect.service.commons.mortar;
//
//import android.content.Context;
//import android.view.View;
//
//import architect.History;
//import architect.Processing;
//import architect.hook.mortar.MortarProcessing;
//import architect.service.Presenter;
//
///**
// * Created by lukasz on 26/11/15.
// */
//public class MortarPresenterProvider implements Presenter.Provider {
//
//    @Override
//    public Context getContext(View container, History.Entry entry, Processing processing) {
//        return MortarProcessing.getScope(processing, entry).createContext(container.getContext());
//    }
//}
