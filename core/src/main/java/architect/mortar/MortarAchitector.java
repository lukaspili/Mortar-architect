//package architect.mortar;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//import architect.Architect;
//import architect.ArchitectedScope;
//import mortar.MortarScope;
//import mortar.bundler.BundleServiceRunner;
//
///**
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class MortarAchitector {
//
//    public static MortarScope onCreateScope(Activity activity, Bundle savedInstanceState, Factory factory) {
//        final String scopeName = activity.getLocalClassName() + "-task-" + activity.getTaskId();
//        MortarScope scope = MortarScope.findChild(activity.getApplicationContext(), scopeName);
//        if (scope == null) {
//            final MortarScope parentScope = MortarScope.getScope(activity.getApplicationContext());
//            final Architect architect = factory.createArchitect();
//
//            MortarScope.Builder builder = parentScope.buildChild()
//                    .withService(BundleServiceRunner.SERVICE_NAME, new BundleServiceRunner())
//                    .withService(Architect.SERVICE_NAME, architect);
//            factory.configureScope(builder, parentScope);
//            scope = builder.build(scopeName);
////            scope.register(architect);
//        }
//
//        BundleServiceRunner.getBundleServiceRunner(scope).onCreate(savedInstanceState);
//
//        return scope;
//    }
//
//    public interface Factory extends ArchitectedScope {
//
//        Architect createArchitect();
//    }
//}
