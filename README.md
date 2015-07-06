# Mortar Architect

"Mortar provides a simplified, composable overlay for the Android lifecycle, to aid in the use of Views as the modular unit of Android applications." (quote from Mortar)  

Architect provides a flexible stack for displaying and organising the flow of views and its Mortar scopes. It organises the mortar scopes in stacks. A stack can be a navigation stack (with its backstack history) or a scope included in another scope (subscopes). 

Architect works only with Mortar, and it's a replacement of the Flow library.  
The motivation behind Architect is to provid a context for building MVP apps with Mortar, with the minimum friction and boilerplate code.  

Where Flow can in theory work without Mortar, Architect relies heavely on Mortar and the scope philosophy. The result is an elegant and powerful API that gets the better of Mortar, while still providing a simple and seamless integration.


## Getting started

For each mortar scope managed by Architect, you must provide a stack scope class.
Stack scope is a class that implements `StackScope` and that is used by Architect to build and configure a mortar scope. It is where you would also build the injection graph. See an example that setups a mortar scope with a dagger2 component and a module.

```java

public class HomeScope implements StackScope {

    private final String name;

    public HomeStackScope(String name) {
        this.name = name;
    }

    @Override
    public Services withServices(MortarScope parentScope) {
    	// the services that it will put in the mortar scope
        return new Services().with(DaggerService.SERVICE_NAME, DaggerHomeStackScope_Component.builder()
                .component(parentScope.<MainActivity.Component>getService(DaggerService.SERVICE_NAME))
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

    @dagger.Component(dependencies = MainActivity2Component.class, modules = Module.class)
    @DaggerScope(Component.class)
    public interface Component {
        void inject(HomeView view);
    }
}
```


## Navigator

`Navigator` is the class that handles the navigation between scopes. The public API is similar to Flow library, but under the hood the implementation is very different.

Each `Navigator` is associated to a delegate class that you must hook up on the container lifecycle. Usually it's the root activity, but it can also be a ViewPresenter if you want to have sub-navigation somewhere else.  
See the calls on `Navigator.delegate()` in the `MainActivity` class, in the example project.

With architect, the navigation is made between `StackPath` classes. `StackPath` is the link between a stack scope and a view. It is also parcelable (using parcelableplease), in order to be persisted and restored in the savedInstanceState bundle when Android kills the app process because of low memory.  
Because `StackPath` classes are boring boilerplate, architect provides an annotation processor **auto-path** that generates paths classes automatically. Apply `@AutoPath` on a `StackScope` class, and voilà.

Once you have your path class, push it to navigator: `Navigator.get(context).push(new HomePath("Hello lukasz"));`


## Auto stack

Auto-stack is another annotation library. It generates `StackScope` classes that configure dagger2 component. It's another step in the goal of reducing the boilerplate code to the minimum.

By annotating a view presenter with `@AutoStack`, it will genereate for you the `StackScope` class, with its dagger2 component and module. You can use it together with `@AutoPath` to generate also the `StackPath`.

For the dagger2 component and module generation, `@AutoStack` relies on Auto dagger2 project.

If you want to pass parameters from one presenter to another, annotate the presenter constructor param with `@StackParam`, and generated stack scope and stack path classes will take it in account.

```java
// it will generate:
// - SlidePageScope
// - SlidePagePath
@AutoStack(
    component = @AutoComponent(dependencies = MainActivity.Component.class),
    path = @AutoPath(withView = SlidePageView.class)
)
@DaggerScope(SlidePagePresenter.class)
public class SlidePagePresenter extends ViewPresenter<SlidePageView> {

    private final int id;

    public SlidePagePresenter(@StackParam int id) {
        this.id = id;
    }
}
```


## Commons

Architect-commons provides some base classes to work with architect stack. See the example project.


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
    def architect_version = '0.10-SNAPSHOT'

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


## Author

- Lukasz Piliszczuk ([@lukaspili](https://twitter.com/lukaspili))


## License

Mortar Architect is released under the MIT license. See the LICENSE file for details.