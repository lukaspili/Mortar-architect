//package architect.service.presentation;
//
//import android.view.ViewGroup;
//
//import architect.Executor;
//import architect.service.Registration;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public abstract class Presentation implements Registration<PresentationController, PresentationPresenter> {
//
//    public abstract void configurePresenter(PresentationPresenter presenter);
//
//    @Override
//    public PresentationController createService(String name, Executor executor) {
//        return new PresentationController(name, executor);
//    }
//
//    @Override
//    public PresentationPresenter createPresenter(ViewGroup view) {
//        PresentationPresenter p = new PresentationPresenter(view);
//        configurePresenter(p);
//        return p;
//    }
//}
