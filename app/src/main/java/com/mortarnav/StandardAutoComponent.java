package com.mortarnav;

import com.mortarnav.deps.WithAppDependencies;

import autodagger.AutoComponent;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(
        dependencies = MainActivity.class,
        superinterfaces = WithAppDependencies.class)
public @interface StandardAutoComponent {
}
