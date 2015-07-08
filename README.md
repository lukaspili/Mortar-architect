# Mortar Architect

Mortar Architect provides a flexible stack for navigating and displaying views and their presenters, using the MVP pattern with [Mortar](https://github.com/square/mortar).

Architect is Mortar scope-centric. A Mortar scope is the glue between a View and its ViewPresenter. Architect will create a Mortar scope for each View & ViewPresenter association. A `StackScope` is a class that Architect will look for when building a Mortar scope, and its role is to configure the Mortar scope.  


## Stack Scope

For each View & ViewPresenter association, you need to provide a `StackScope` class. 
Below, an example of a `HomePresenter`, `HomeView` and `HomeStackScope` that uses Dagger2.

```java
// HomePresenter.java
public class HomePresenter extends ViewPresenter<HomeView> {

}


// HomeView.java
public class HomeView extends FrameLayout {
    
    @Inject
    protected HomePresenter presenter;

    public HomeView(Context context) {
        super(context);

        DaggerService.<HomeStackScope.Component>get(context).inject(this);

        View view = View.inflate(context, R.layout.home_view, this);
        ButterKnife.inject(view);
    }

    // onAttachedToWindow()
    // onDetachedFromWindow()
}


// HomeStackScope

public class HomeStackScope implements StackScope {

    @Override
    public Services withServices(MortarScope parentScope) {
        // put the Dagger2 component in the Mortar scope
        return new Services().with(DaggerService.SERVICE_NAME,
            DaggerHomeStackScope_Component.builder()
                    .rootActivity(parentScope.<RootActivity.Component>getService(DaggerService.SERVICE_NAME))
                    .module(new Module())
                    .build());
    }

    @dagger.Module
    public class Module {

        @Provides
        @DaggerScope(Component.class)
        public HomePresenter providesPresenter() {
            return new HomePresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.Component.class, modules = Module.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(HomeView view);
    }
}
```

## Auto Stack & Auto Dagger2

Because writing a class that implements `StackScope` for every ViewPresenter is boring and usually boilerpate, Architect provides an annotation processor that generates the `StackScope` class for you. It also relies on [Auto Dagger2](https://github.com/lukaspili/Auto-Dagger2) for generating the Dagger2 components.

```java
// HomePresenter.java
@AutoStack(
        component = @AutoComponent(dependencies = RootActivity.Component.class)
)
@DaggerScope(HomePresenter.class)
public class HomePresenter extends ViewPresenter<HomeView> {

}

// It will generate the HomeStackScope.java for you
// And you still have to write HomeView.java yourself
```


## Stack Path

Architect provides a complete stack for navigation between Mortar scopes (View & ViewPresenter). However, it requires an additional class that extends from `StackPath`. The `StackPath` class contains a bunch of boilerplate, implements `Parcelable` and allows Architect to persist the parameters that you can pass between ViewPresenters. It allows then to restore the navigation stack after Android kills your application process.

For the `HomeViewPresenter` above, here what looks like `HomePath`.  
Note that we use [ParcelablePlease](https://github.com/sockeqwe/ParcelablePlease), which is an annotation processor that generates the boilerplate code required by `Parcelable`.

```java
@ParcelablePlease
public class HomePath extends StackPath<HomeStackScope> {

    public HomePath() {
        
    }

    private HomePath(Parcel parcel) {
        super(parcel);
    }

    @Override
    public HomeStackScope withScope() {
        return new HomeStackScope(name);
    }

    @Override
    public View withView(Context context) {
        return new HomeView(context);
    }

    @Override
    protected void readParcel(Parcel parcel) {
        HomePathParcelablePlease.readFromParcel(this, parcel);
    }

    @Override
    protected void writeParcel(Parcel parcel) {
        HomePathParcelablePlease.writeToParcel(this, parcel, 0);
    }

    public static final Parcelable.Creator<HomePath> CREATOR = new Parcelable.Creator<HomePath>() {
        public HomePath createFromParcel(Parcel in) {
            return new HomePath(in);
        }

        public HomePath[] newArray(int size) {
            return new HomePath[size];
        }
    };
}
```

## Auto Path

In the same way that works Auto Stack, Architects provides an annotation processor that generates the `StackPath` class for you.  

You can either generate a `StackPath` class from a manually written `StackScope` class, or directly from the ViewPresenter using both `@AutoStack` and `@AutoPath`.

```java
@AutoPath(withView = HomeView.class)
public class HomeStackScope implements StackScope {

    @Override
    public Services withServices(MortarScope parentScope) {
        // put the Dagger2 component in the Mortar scope
        return new Services().with(DaggerService.SERVICE_NAME,
            DaggerHomeStackScope_Component.builder()
                    .rootActivity(parentScope.<RootActivity.Component>getService(DaggerService.SERVICE_NAME))
                    .module(new Module())
                    .build());
    }

    @dagger.Module
    public class Module {

        @Provides
        @DaggerScope(Component.class)
        public HomePresenter providesPresenter() {
            return new HomePresenter();
        }
    }

    @dagger.Component(dependencies = RootActivity.Component.class, modules = Module.class)
    @DaggerScope(Component.class)
    public interface Component {

        void inject(HomeView view);
    }
}
```

However, the recommended way is to generate both the `StackScope` and the `StackPath` classes from the ViewPresenter:

```java
@AutoStack(
        component = @AutoComponent(dependencies = RootActivity.Component.class),
        path = @AutoPath(withView = HomeView.class)
)
@DaggerScope(HomePresenter.class)
public class HomePresenter extends ViewPresenter<HomeView> {

}
```


## Navigation

Architect provides the `Navigator` class that let you navigate between paths.  
It manages a history stack, allows you to provide custom transitions between views, and is able to survive configuration changes and process kills.

`Navigator` lives inside his own Mortar scope, and you can retreive its instance through a child scope, from a View or a Context wrapped by Mortar.

```java
    Navigator.get(getView()).push(new ShowUserPath("lukasz"));
```

`Navigator` provides 4 navigation methods

### Navigator.push()

The common navigation way, that push the new path in the navigation history.
It will perform the view transition from the previous view to the new view.
Once the transition is done, the previous view will be removed and destroyed. However, its Mortar scope won't be destroyed (and so neither its ViewPresenter).

### Navigator.show()

The way when you want to show a "modal" view.  
It works the same way as `push()`, but the difference is that the previous view won't be removed at the end of the view transition.

It's useful when you want to for instance to show a View on top of the previous one, while not taking the whole screen. So you would want that the previous view is not removed and still visible.

### Navigator.replace()

It replaces the current view by the new one.  
It means that the previous view won't be in the history stack.

### Navigator.back()

It goes back into the history stack.  
It will perform the `backward()` view transition, and then remove the old view and destroy its Mortar scope.

### Navigator.chain()

Lets you execute several navigation event, in a sequential order.


## View Transitions

You can provide a `TransitionsMapping` to the `Navigator` that tells what view transition perform when navigating from one view to another.

```java
TransitionsMapping()
    .byDefault(new LateralViewTransition()) // default transition
    .show(MyPopupView.class).withTransition(new FadeModalTransition(new Config().duration(250))) // by default, it's show().fromAny()
    .show(MyOtherScreen.class).from(HomeView.class).withTransition(new BottomAppearTransition()); // you can also specify show().from() specific view
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


## Navigation params

You often would want to pass parameters when navigation from a path to another.  
Auto-path handles it nicely. You only need to annotate the navigation parameters expected by a ViewPresenter.

```java
@AutoStack(
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

    // NOTE the @StackParam on the parameter provided by the navigation
    public ShowUserPresenter(@StackParam String username, RestClient restClient, UserManager userManager) {
        this.username = username;
        this.restClient = restClient;
        this.userManager = userManager;
    }
}
```

Auto-path and auto-stack will generate the appropriate `ShowUserScope` and `ShowUserPath` that requires the username parameter. When you will navigate to the `ShowUserPath`, you will have to provide the username.

```java
    Navigator.get(getView()).push(new ShowUserPath("lukasz"));
```


## Returns result

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
The second option is to use the `architect-commons` subproject, make the activity extends from `ArchitectActivity` and implements the several required methods. You can find an example in [`MainActivity2`](https://github.com/lukaspili/Mortar-architect/blob/master/app/src/main/java/com/mortarnav/MainActivity2.java) class.

The same applies for the Application class, check out the [`App`](https://github.com/lukaspili/Mortar-architect/blob/master/app/src/main/java/com/mortarnav/App.java) and [`App2`](https://github.com/lukaspili/Mortar-architect/blob/master/app/src/main/java/com/mortarnav/App.java) (which extends from `ArchitectApp`) classes.


### Don't restore navigation stack after process kill

With `Navigator`, you can choose to not restore the navigation stack when the application process is killed. By default this option is not enabled.

The very big advantage of this option is enabled is that you won't have to bother with the `savedInstanceState Bundle` in the ViewPresenter `onLoad(savedInstanceState)` and `onSave(Bundle outState)`.

Indeed, because ViewPresenter instances survive configuration changes, the only case where you would save and restore ViewPresenter instance from the `Bundle` class is when Android kills your application process. The next time you would open the app, `Navigator` would restore your navigation stack, and thus it would be your responsability to restore your ViewPresenter states.

In opposite, when the "don't restore navigation stack" option is enabled, `Navigator` will not restore the navigation stack if the process is killed, but will start the app from the initial state. So you would never use the `savedInstanceState Bundle` in your ViewPresenters.

To enable the option, provide a custom configuration when creating the `Navigator` instance:

```java
    Navigator navigator = Navigator.create(scope, new Navigator.Config().dontRestoreStackAfterKill(true));
```


## Sub navigator

Architect is very flexible and you can use several `Navigator` instances at the same time. It allows to provide sub navigation in your app.

You can find an example of a sub navigator configured in a ViewPresenter in the [`SubnavPresenter`](https://github.com/lukaspili/Mortar-architect/blob/master/app/src/main/java/com/mortarnav/presenter/SubnavPresenter.java) class.


## Composition

With Architect, you can easily stack several Mortar scopes.  
By stacking scopes, it means that you would for instance include a View-ViewPresenter inside another one.

For instance, we want to include the `HomeMenuView` (which has its `HomeMenuPresenter`) inside the `HomeView`.

First let's create the `HomeMenuPresenter` and `HomeMenuView`

```java
// HomeMenuPresenter.java

@AutoStack(
        component = @AutoComponent(dependencies = HomePresenter.class)
        // because we don't need HomeMenuPresenter in navigation, we don't need to generate the StackPath class
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


## Commons

Commons is a facultative sub project that provides some base class you can extend from, in order to save some boilerplate code.

 * [`ArchitectApp`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/ArchitectApp.java)
 * [`ArchitectActivity`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/ArchitectActivity.java)
 * `PresentedXXX`, like [`PresentedFrameLayout`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/view/PresentedFrameLayout.java), [`PresentedLinearLayout`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/view/PresenterLinearLayout.java), etc. Base class for a View associated to a ViewPresenter.
 * `StackedXXX`, like [`StackedFrameLayout`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/view/StackedFrameLayout.java), [`StackedLinearLayout`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/view/StackedLinearLayout.java), etc. Base class for the a View associated to a ViewPresenter, and that will be included (stacked) in another one (like the `HomeMenuView`)
 * [`StackPagerAdapter`](https://github.com/lukaspili/Mortar-architect/blob/master/commons/src/main/java/architect/commons/adapter/StackPagerAdapter.java), an implementation of `ViewPager` that manages a set of `StackPath`

The commons project is here both for easing the integration and providing an example of implementations that work well with Mortar and Architect. The code is very simple and straightforward.


## Demo projects

 * The subproject **app** which showcases all the features offered by Architect
 * [Mortar architect map demo](https://github.com/lukaspili/Mortar-architect-map-demo) which showcase how to use MapView and DrawerLayout with Architect

You can also checkout the following example projects using Mortar and Flow. It may give you better understanding on how works Mortar and Flow together, and thus the purpose of Architect:

 * [Mortar Flow Dagger2 demo](https://github.com/lukaspili/Mortar-Flow-Dagger2-demo)
 * [Power Mortar Flow Dagger2 demo](https://github.com/lukaspili/power-Mortar-Flow-Dagger2-demo)


## Motivation

The motivation behind Architect is to provide a framework for building MVP apps with Mortar, with the minimum friction and boilerplate code.  

While Flow can in theory work without Mortar, Architect relies heavely on Mortar and its "scope philosophy". The practical consequence is that Mortar Architect provides an API that relies on Mortar and integrates great within it.


## Installation

Library is divided in several dependencies, allowing to use only specific features if you don't want to use the whole package. Only core library is required.

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
    def architect_version = '0.11-SNAPSHOT'

    // Core library
    compile 'com.github.lukaspili.mortar-architect:architect:' + architect_version

    // Commons
    compile 'com.github.lukaspili.mortar-architect:commons:' + architect_version

    // Auto path
    compile 'com.github.lukaspili.mortar-architect:autopath:' + architect_version
    apt 'com.github.lukaspili.mortar-architect:autopath-compiler:' + architect_version

    // Auto path requires parcelable please deps
    compile 'com.hannesdorfmann.parcelableplease:annotation:1.0.1'
    apt 'com.hannesdorfmann.parcelableplease:processor:1.0.1'

    // Auto scope
    compile 'com.github.lukaspili.mortar-architect:autoscope:' + architect_version
    apt 'com.github.lukaspili.mortar-architect:autoscope-compiler:' + architect_version

    // Auto scope requires dagger2 and auto dagger2 deps
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

The core API is stable and tested in several soon to be in production apps.  
Minor changes can still be expected, and there is no garantee for a backward compatibility until it reaches the 1.0 milestone. 


## Author

- Lukasz Piliszczuk ([@lukaspili](https://twitter.com/lukaspili))


## License

Mortar Architect is released under the MIT license. See the LICENSE file for details.