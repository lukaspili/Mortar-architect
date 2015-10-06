//package architect;
//
//import android.view.View;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Views transitions mapping
// *
// * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
// */
//public class TransitionsMapping {
//
//    final List<Mapping> list = new ArrayList<>();
//
//    public TransitionsMapping byDefault(ViewTransition transition) {
//        return new Mapping(null).withTransition(transition);
//    }
//
//    /**
//     * Create new transition mapping to the destination view
//     */
//    public Mapping show(Class<? extends View> view) {
//        return new Mapping(view);
//    }
//
//    public class Mapping {
//
//        Class<? extends View> view;
//        Class<? extends View>[] from;
//        ViewTransition transition;
//
//        private Mapping(Class<? extends View> view) {
//            this.view = view;
//        }
//
//        /**
//         * Specify specific originating views
//         * If you want that to be any view, don't call this method
//         */
//        public Mapping from(Class<? extends View>... from) {
//            Preconditions.checkArgument(from != null && from.length > 0, "From views cannot be null or empty");
//            this.from = from;
//            return this;
//        }
//
//        public TransitionsMapping withTransition(ViewTransition transition) {
//            Preconditions.checkNotNull(transition, "Screen transition cannot be null");
//            this.transition = transition;
//            return build();
//        }
//
//        public TransitionsMapping withoutTransition() {
//            return build();
//        }
//
//        private TransitionsMapping build() {
//            list.add(this);
//            return TransitionsMapping.this;
//        }
//    }
//}
