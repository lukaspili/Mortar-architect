package com.mortarnav;

import android.content.Context;

import mortarnav.library.Screen;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class ScreenA extends Screen<ViewA> {

    @Override
    public ViewA withView(Context context) {
        return new ViewA(context);
    }



    //
//
//    @dagger.Component(dependencies = MainActivity.Component.class, modules = Module.class)
//    public class Component {
//
//
//    }
//
//    @dagger.Module
//    public class Module {
//
//    }
}
