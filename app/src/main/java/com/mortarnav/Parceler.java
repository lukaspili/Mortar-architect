package com.mortarnav;

import android.os.Parcelable;

import org.parceler.Parcels;

import architect.ScreenPath;
import architect.StackableParceler;

/**
 * PathParceler that uses Parceler library
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class Parceler implements StackableParceler {

    @Override
    public Parcelable wrap(ScreenPath path) {
        return Parcels.wrap(path);
    }

    @Override
    public ScreenPath unwrap(Parcelable parcelable) {
        return Parcels.unwrap(parcelable);
    }
}
