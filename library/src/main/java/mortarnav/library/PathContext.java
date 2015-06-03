//package mortarnav.library;
//
//import android.content.Context;
//import android.content.ContextWrapper;
//
///**
// * Inspired from flow-path
// * More simplified version without the Map<Path, Context> contexts
// *
// * @author lukasz piliszczuk - lukasz.pili@gmail.com
// */
//public final class PathContext extends ContextWrapper {
//    private static final String SERVICE_NAME = "PATH_CONTEXT";
//    private final Path path;
//
//    PathContext(Context baseContext, Path path) {
//        super(baseContext);
//
//        Preconditions.checkArgument(baseContext != null, "Leaf context may not be null.");
//        this.path = path;
//    }
//
//    public static PathContext root(Context baseContext) {
//        return new PathContext(baseContext, Path.ROOT);
//    }
//
//    public static PathContext create(Path path, PathContext parent, PathContextFactory factory) {
//        if (path == Path.ROOT) throw new IllegalArgumentException("Path is empty.");
//        return new PathContext(factory.setUpContext(path, parent), path);
//    }
//
//    @SuppressWarnings("ResourceType")
//    public static PathContext get(Context context) {
//        return Preconditions.checkNotNull((PathContext) context.getSystemService(SERVICE_NAME),
//                "Expected to find a PathContext but did not.");
//    }
//
//    @Override
//    public Object getSystemService(String name) {
//        if (SERVICE_NAME.equals(name)) {
//            return this;
//        }
//        return super.getSystemService(name);
//    }
//}