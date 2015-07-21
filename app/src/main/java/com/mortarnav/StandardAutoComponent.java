package com.mortarnav;

import com.mortarnav.deps.WithActivityDependencies;
import com.mortarnav.deps.WithAppDependencies;

import autodagger.AutoComponent;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@AutoComponent(
        dependencies = MainActivity.class,
        superinterfaces = WithActivityDependencies.class)
public @interface StandardAutoComponent {
}
