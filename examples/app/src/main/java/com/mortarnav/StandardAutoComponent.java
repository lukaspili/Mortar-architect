package com.mortarnav;

import architect.examples.simple_app.deps.WithActivityDependencies;
import architect.examples.simple_app.deps.WithAppDependencies;

import autodagger.AutoComponent;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(
        dependencies = MainActivity.class,
        superinterfaces = WithActivityDependencies.class)
public @interface StandardAutoComponent {
}
