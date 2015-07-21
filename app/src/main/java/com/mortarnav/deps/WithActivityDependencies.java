package com.mortarnav.deps;

import com.mortarnav.ToolbarOwner;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public interface WithActivityDependencies extends WithAppDependencies {

    ToolbarOwner toolbarOwner();
}
