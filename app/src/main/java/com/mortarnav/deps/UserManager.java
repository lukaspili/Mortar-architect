package com.mortarnav.deps;

import com.mortarnav.App2;
import com.mortarnav.DaggerScope;

import javax.inject.Inject;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@DaggerScope(App2.class)
public class UserManager {

    @Inject
    public UserManager() {
    }
}
