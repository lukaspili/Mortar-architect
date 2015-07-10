package com.mortarnav.deps;

import com.mortarnav.App;
import com.mortarnav.DaggerScope;

import javax.inject.Inject;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@DaggerScope(App.class)
public class RestClient {

    @Inject
    public RestClient() {
    }
}
