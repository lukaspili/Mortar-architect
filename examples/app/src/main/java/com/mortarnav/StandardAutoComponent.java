package com.mortarnav;

import architect.examples.mortar_app.deps.WithActivityDependencies;
import architect.examples.mortar_app.deps.WithAppDependencies;

import autodagger.AutoComponent;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(
        dependencies = MainActivity.class,
        superinterfaces = WithActivityDependencies.class)
public @interface StandardAutoComponent {
}
