package com.mortarnav;

import android.os.Parcelable;

import org.parceler.Parcels;

import architect.ScreenPath;
import architect.ScreenParceler;

/**
 * PathParceler that uses Parceler library
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Parceler implements ScreenParceler {

    @Override
    public Parcelable wrap(ScreenPath path) {
        return Parcels.wrap(path);
    }

    @Override
    public ScreenPath unwrap(Parcelable parcelable) {
        return Parcels.unwrap(parcelable);
    }
}
