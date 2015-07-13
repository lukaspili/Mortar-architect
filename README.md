# Mortar Architect

Mortar Architect helps building modern Android apps, implementing the MVP pattern with [Mortar](https://github.com/square/mortar).

When working with Mortar and MVP pattern, you don't create Activities and Fragments anymore, but Android Views and ViewPresenters. Each ViewPresenter is associated to a `MortarScope` that holds and provide the `ViewPresenter` to its associated `View`.

If you use Dagger2, it would be the Dagger2 Component that holds and provides the `ViewPresenter` instance, and the `MortarScope` would hold and provide the Component instance.

Architect provides tools for navigating between Mortar scopes, and nesting Mortar scopes. It's more feature complete and ready to use than the "official" library [Flow](https://github.com/square/flow). It also requires much less code to write and it integrates seamlessly with [Mortar](https://github.com/square/mortar).


## Stackable

Because Architect relies on Mortar scopes, it also require a class that setups those scopes. For each `MortarScope` that will be associated to a `View` and `ViewPresenter`, you have to provide a `Stackable` class that configures the Mortar scope.

The following `Stackable` class creates a Dagger2 component, and puts it inside the `MortarScope`. The Dagger2 component provides the `HomePresenter`, which is an instance of `ViewPresenter`.

```java
@DaggerScope(Component.class)
public class HomeStackable implements Stackable {

    private String name;

    public HomeStackable(String name) {
        this.name = name;
    }

    @Override
    public void configureScope(MortarScope.Builder builder, MortarScope parentScope) {
        builder.withService(DaggerService.SERVICE_NAME, DaggerHomePath_Component.builder()
                .mainActivityComponent(parentScope.<MainActivityComponent>getService(DaggerService.SERVICE_NAME))
                .module(new Module())
                .build());
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(Component.class)
        public HomePresenter providesPresenter() {
            return new HomePresenter(name);
        }
    }

    @dagger.Component(dependencies = MainActivityComponent.class, modules = Module.class)
    @DaggerScope(Component.class)
    public interface Component {
        void inject(HomeView view);
    }
}
```

With a `Stackable`, you can create new `Context` that contains the associated `MortarScope`. Architect takes care of the Mortar scope creation, and ensures the scope name is preserved during config changes.

```java
public class HomeView extends FrameLayout {

    protected HomePresenter presenter;

    public HomeView(Context context, AttributeSet attrs) {
        Context newContext = StackFactory.createContext(context, new HomeStackable());
        View.inflate(newContext, R.layout.home_view, this);
        DaggerService.<HomeStackable.Component>get(newContext).inject(this);
    }
}
```


## Navigation

Architect `Navigator` class allows you to navigate between Mortar scopes. It manages a history stack that preserves previous Mortar scope, allows you to provide custom transitions between views, and survives configuration changes and process kills.

`Navigator` lives inside its own Mortar scope, and you can retreive its instance through a child scope, from a View or a Context wrapped by Mortar.

```java
    Navigator.get(context).push(new ShowUserStackable("lukasz"));
```

`Stackable` class does not specify which is the associated View to display, because you directly use the `Stackable` inside the View. For navigation, you need to implements the `StackablePath` interface, that extends from `Stackable` and declares one additional method:

```java
public interface StackablePath extends Stackable {

    View createView(Context context);
}
```

The following `HomeStackable` implements now `StackablePath`. Nothing else changed from the code above.

```java
@DaggerScope(Component.class)
public class HomeStackable implements StackablePath {

    View createView(Context context) {
        return new HomeView(context);
    }

    // ...
}
```

Which is now compatible with `Navigator`:

```java
    Navigator.get(getView()).push(new HomeStackable("first home"));
```


`Navigator` provides 4 navigation methods

#### `Navigator.push()`

The common navigation way, that push the new path in the navigation history.
It will perform the view transition from the previous view to the new view.
Once the transition is done, the previous view will be removed and destroyed. However, its Mortar scope won't be destroyed (and so neither its ViewPresenter).

#### `Navigator.show()`

The way when you want to show a "modal" view.  
It works the same way as `push()`, but the difference is that the previous view won't be removed at the end of the view transition.

It's useful when you want to for instance to show a View on top of the previous one, while not taking the whole screen. So you would want that the previous view is not removed and still visible.

#### `Navigator.replace()`

It replaces the current view by the new one.  
It means that the previous view won't be in the history stack.

#### `Navigator.back()`

It goes back into the history stack.  
It will perform the `backward()` view transition, and then remove the old view and destroy its Mortar scope.

#### `Navigator.chain()`

Lets you execute several navigation event, in a sequential order.


## View Transitions

You can provide a `TransitionsMapping` to the `Navigator` that defines what view transition perform when navigating from one view to another.

```java
navigator.transitions().register(TransitionsMapping()
    .byDefault(new LateralViewTransition()) // default transition
    .show(MyPopupView.class).withTransition(new FadeModalTransition(new Config().duration(250))) // by default, it's show().fromAny()
    .show(MyOtherScreen.class).from(HomeView.class).withTransition(new BottomAppearTransition())); // you can also specify show().from() specific view
```

Once the mapping is provided to the `Navigator` instance, it will apply the correct view transitions automatically.

You can also create and provide your custom view transitions.  
There is basically two types of transitions: 

 * `ViewTransition` where you can animate the enter view and the exit view.
 * `ModalTransition` where you can animate only the enter view. `ModalTransition` is just a subclass of `ViewTransition`, here for your convenience.

Example of `ViewTransition`:

```java
// LateralViewTransition.java

public class LateralViewTransition extends BaseViewTransition<View, View> {

    public LateralViewTransition() {

    }

    public LateralViewTransition(Config config) {
        super(config);
    }

    @Override
    public void forward(View enterView, View exitView, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_X, enterView.getWidth(), 0));
        set.play(ObjectAnimator.ofFloat(exitView, View.TRANSLATION_X, 0, -exitView.getWidth()));
    }

    @Override
    public void backward(View enterView, View exitView, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(enterView, View.TRANSLATION_X, -enterView.getWidth(), 0));
        set.play(ObjectAnimator.ofFloat(exitView, View.TRANSLATION_X, 0, exitView.getWidth()));
    }
}
```

Example of `ModalTransition`:

```java
// BottomAppearTransition.java

public class BottomAppearTransition extends BaseModalTransition<View> {

    public BottomAppearTransition() {
    }

    public BottomAppearTransition(Config config) {
        super(config);
    }

    @Override
    public void show(View view, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.getHeight(), 0));
    }

    @Override
    public void hide(View view, AnimatorSet set) {
        set.play(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, view.getHeight()));
    }

    @Override
    public boolean hideExitView() {
        return true; // hides the exit view once the view transition is finished
    }
}
```


## Return result

A ViewPresenter can return a result to the previous ViewPresenter in the history. A kind of `onActivityResult()` between ViewPresenters.

Let's say you navigated from PresenterA to PresenterB, and now PresenterB wants to return a String result to PresenterA:

```java
// PresenterB.java
Navigator.get(getView()).back("My result!");
```

PresenterA must implement the `ReceivesResult` interface:

```java
// PresenterA.java
public class PresenterA extends ViewPresenter<AView> implements ReceivesResult<String> {

    private String result;

    @Override
    public void onReceivedResult(String result) {
        this.result = result;
        // beware that this is called before onLoad() and getView() returns null here
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        // onLoad() is called when we go back from PresenterB to PresenterA
        if(result != null) {
            getView().getTitleTextView().setText(result);
        }
    }
}
```

You must also ensures that the View associated to the ViewPresenter that receives the result implements `HasPresenter` interface. It is already the case for all the base views of the **architect-commons** subproject.

```java
public class AView extends LinearLayout implements HasPresenter<PresenterA> {
    
    @Inject
    protected PresenterA presenter;

    @Override
    public PresenterA getPresenter() {
        return presenter;
    }
}
```


## Architect and Navigator configuration

Before using `Navigator`, you need to configure and hook it to the root activity.  
You need to call the `Navigator.delegate()` methods at the proper place.

You can find an example of configuration in the [`MainActivity`](https://github.com/lukaspili/Mortar-architect/blob/master/app/src/main/java/com/mortarnav/MainActivity.java) class.  
**architect-commons** subproject provides the `ActivityArchitector` class that takes care of some boilerplate required to setup Architect in the root activity.


### StackableParceler

In order to survive process kills, and restore the navigation stack, `Navigator` requires a `StackableParceler` that saves and restore the `StackablePath` from disk with the help of Android `Parcelable`.

```java
    Navigator navigator = Navigator.create(scope, new Parceler()); // Parceler is a class that implements StackableParceler
```

The most performant solution is to make your `StackablePath` classes compatible with `Parcelable`. You have several options, like:

 * Making your stackable paths implement `Parcelable` which adds tons of boilerplate
 * Use a library that takes of the boilerplate for you, like [Parceler](https://github.com/johncarl81/parceler)

Below an example of the second solution:

```java
// Some StackablePath
@Parcel(parcelsIndex = false)
public class HomeStackable implements StackablePath {

    String name;

    @ParcelConstructor
    public HomeStackable(String name) {
        this.name = name;
    }

    ...
}


// StackableParceler
public class Parceler implements StackableParceler {

    @Override
    public Parcelable wrap(StackablePath path) {
        return Parcels.wrap(path);
    }

    @Override
    public StackablePath unwrap(Parcelable parcelable) {
        return Parcels.unwrap(parcelable);
    }
}
```

That's it! Boilerplate to the minimum.


### Don't restore navigation stack after process kill

With `Navigator`, you can choose to not restore the navigation stack when the application process is killed. By default this option is not enabled.

The very big advantage of this option is enabled is that you won't have to bother with the `savedInstanceState Bundle` in the ViewPresenter `onLoad(savedInstanceState)` and `onSave(Bundle outState)`.

Indeed, because ViewPresenter instances survive configuration changes, the only case where you would save and restore ViewPresenter instance from the `Bundle` class is when Android kills your application process. The next time you would open the app, `Navigator` would restore your navigation stack, and thus it would be your responsability to restore your ViewPresenter states.

In opposite, when the "don't restore navigation stack" option is enabled, `Navigator` will not restore the navigation stack if the process is killed, but will start the app from the initial state. So you would never use the `savedInstanceState Bundle` in your ViewPresenters.

To enable the option, provide a custom configuration when creating the `Navigator` instance:

```java
    Navigator navigator = Navigator.create(scope, new Parceler(), new Navigator.Config().dontRestoreStackAfterKill(true));
```


## Nested navigator

Architect is very flexible and you can use several `Navigator` instances at the same time. It allows to provide nested navigation in your app.

You can find an example of a sub navigator configured in a ViewPresenter in the [`SubnavPresenter`](https://github.com/lukaspili/Mortar-architect/blob/master/app/src/main/java/com/mortarnav/presenter/SubnavPresenter.java) class.


## Nesting stackables

With Architect, you can easily nest several Stackables.
You would for instance want to include a Stackable inside another one.

The following `HomeMenuStackable` is nested in the `HomeStackable`.

```java
// HomeMenuPresenter.java

@AutoStackable(
        component = @AutoComponent(dependencies = HomePresenter.class)
)
@DaggerScope(HomeMenuPresenter.class)
public class HomeMenuPresenter extends ViewPresenter<HomeMenuView> {

    private final HomePresenter homePresenter;

    @Inject
    public HomeMenuPresenter(HomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {

    }
}


// HomeMenuView.java

@AutoInjector(HomeMenuPresenter.class)
public class HomeMenuView extends FrameLayout {

    @Inject
    protected HomeMenuPresenter presenter;

    public HomeMenuView(Context context, AttributeSet attrs) {
        // create new Mortar wrapped context for the HomeMenuScope
        Context newContext = StackFactory.createContext(context, new HomeMenuScope());

        DaggerService.<HomeMenuScopeComponent>get(newContext).inject(this);

        View view = View.inflate(newContext, R.layout.view_home_menu, this);
        ButterKnife.inject(view);
    }

    // onAttachedToWindow()
    // onDetachedFromWindow()
}
```

You can then directly use the `HomeMenuView` in the `HomeView` layout:

```xml
<!-- view_home.xml -->
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
             android:layout_width="match_parent"
             android:layout_height="match_parent">

        <com.example.mvp.presenter.HomeMenuView
            android:id="@+id/menu_view"
            android:layout_width="240dp"
            android:layout_height="match_parent"
            android:layout_gravity="left|start"/>
</FrameLayout>
```


## Architect-commons

Commons is a facultative sub project that provides some base class you can extend from, in order to save some boilerplate code.

 * [`ActivityArchitector`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/ActivityArchitector.java)
 * `PresentedXXX`, like [`PresentedFrameLayout`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/view/PresentedFrameLayout.java), [`PresentedLinearLayout`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/view/PresenterLinearLayout.java), etc. Base class for a View associated to a ViewPresenter.
 * `StackedXXX`, like [`StackedFrameLayout`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/view/StackedFrameLayout.java), [`StackedLinearLayout`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/view/StackedLinearLayout.java), etc. Base class for the a View associated to a ViewPresenter, and that will be included (stacked) in another one (like the `HomeMenuView`)
 * [`StackablePagerAdapter`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/adapter/StackablePagerAdapter.java), an implementation of `ViewPager` that manages a set of `StackablePath`

The commons project is here both for easing the integration and providing an example of implementations that work well with Mortar and Architect. The code is very simple and straightforward.


## Architect-Robot

Robot is a subproject that contains an annotation processor that generates `Stackable` and `StackablePath` classes for you. Robot is opiniated, it works only with Dagger2 and uses [Auto Dagger2](https://github.com/lukaspili/Auto-Dagger2) to generate Dagger2 components.

To generate a `Stackable` from a `ViewPresenter`, use the `@AutoStackable` annotation:

```java
@AutoStackable(
        component = @AutoComponent(includes = StandardAutoComponent.class)
)
@DaggerScope(SlidesPresenter.class)
public class SlidesPresenter extends ViewPresenter<SlidesView> {

}
```

And provide the `pathWithView` member to generate a `StackablePath` instead:

```java
@AutoStackable(
        component = @AutoComponent(includes = StandardAutoComponent.class),
        pathWithView = SlidesView.class
)
@DaggerScope(SlidesPresenter.class)
public class SlidesPresenter extends ViewPresenter<SlidesView> {

}
```


## Navigation params

By default, the generated `StackablePath` will have an empty constructor, and all the parameters of the `ViewPresenter`'s constructor will be provided by Dagger2 in its module.

If you want some parameters to be provided by navigation, use the `@FromPath` annotation.

```java
@AutoStackable(
        component = @AutoComponent(dependencies = RootActivity.Component.class),
        path = @AutoPath(withView = ShowUserView.class)
)
@DaggerScope(ShowUserPresenter.class)
public class ShowUserPresenter extends ViewPresenter<ShowUserView> {

    // username is provided by the navigation
    private final String username;

    // some dependencies provided by dagger
    private final RestClient restClient;
    private final UserManager userManager;

    // NOTE the @FromPath on the parameter provided by the navigation
    public ShowUserPresenter(@FromPath String username, RestClient restClient, UserManager userManager) {
        this.username = username;
        this.restClient = restClient;
        this.userManager = userManager;
    }
}
```

You can then navigate to the new generated `StackablePath`

```java
    Navigator.get(getView()).push(new ShowUserStackable("lukasz"));
```


## Demo projects

 * The subproject **app** which showcases all the features offered by Architect
 * [Mortar architect map demo](https://github.com/lukaspili/Mortar-architect-map-demo) which showcase how to use MapView and DrawerLayout with Architect

You can also checkout the following example projects using Mortar and Flow. It may give you better understanding on how works Mortar and Flow together, and thus the purpose of Architect:

 * [Mortar Flow Dagger2 demo](https://github.com/lukaspili/Mortar-Flow-Dagger2-demo)
 * [Power Mortar Flow Dagger2 demo](https://github.com/lukaspili/power-Mortar-Flow-Dagger2-demo)


## Motivation

The motivation behind Architect is to provide a framework for building MVP apps with Mortar, with the minimum friction and boilerplate code.  

While Flow can in theory work without Mortar, Architect relies heavely on Mortar and Mortar scopes. It allows to provide an API that integrates seamlessly with Mortar.


### Key differences with Flow

The goal is not to say that Architect is better than Flow, but that the 2 libraries handle things differently

 * Architect does not destroy the Mortar scopes in history. It means that the ViewPresenter of a previous View won't be destroyed, and a new View will be re-attached once navigation gets back to it.

 * Architect provides two different ways of navigation: `push` and `show`. The latter allows to push a new View without removing the previous one (useful for showing partial views, like dialogs). Architect handles the view manipulation and restoration during config changes.

 * Navigation events are applied directly on history, without waiting for the ViewTransition to finish. It means that if you rotate the screen during transition from A to B, the screen showed after rotation will be B. In opposite, Flow will start the view transition from A to B again.

 * Architect provides a ViewTransition mapping that let you define how to transition from a View to another, with very little code.

 * Architect allows to have nested navigation.

 * Architect provides convinient methods to nest scopes and views. Like including B into A directly in view's xml.


## Installation

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
		classpath 'com.android.tools.build:gradle:1.1.3'
		classpath 'com.neenbedankt.gradle.plugins:android-apt:1.4'
    }
}

apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'

repositories {
    jcenter()
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}

dependencies {
    // local var convinience for architect version
    def architect_version = '0.13-SNAPSHOT'

    // Core library
    compile 'com.github.lukaspili.mortar-architect:architect:' + architect_version

    // Commons
    compile 'com.github.lukaspili.mortar-architect:commons:' + architect_version

    // Robot
    compile 'com.github.lukaspili.mortar-architect:robot:' + architect_version
    apt 'com.github.lukaspili.mortar-architect:robot-compiler:' + architect_version

    // Robot requires dagger2 and auto dagger2 deps
    // Dagger2
    compile 'com.google.dagger:dagger:2.0.1'
    apt 'com.google.dagger:dagger-compiler:2.0.1'
    provided 'javax.annotation:jsr250-api:1.0'

    // Autodagger2
    compile 'com.github.lukaspili.autodagger2:autodagger2:1.1'
    apt 'com.github.lukaspili.autodagger2:autodagger2-compiler:1.1'
}
```


## Status

The core API should be stable enough. Architect is implemented in several soon to be in production apps.  


## Author

- Lukasz Piliszczuk ([@lukaspili](https://twitter.com/lukaspili))


## License

Mortar Architect is released under the MIT license. See the LICENSE file for details.