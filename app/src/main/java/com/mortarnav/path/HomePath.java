package com.mortarnav.path;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;
import com.mortarnav.nav.HomeScope;
import com.mortarnav.view.HomeView;

import mortarnav.NavigationPath;

/**
 * This is a manually written path, for the sample
 * Recommended and easiest way is to use @AutoPath and let the annotation
 * processor generate for you that boring boilerplate!
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
@ParcelablePlease
public class HomePath extends NavigationPath<HomeScope> {

    String name;

    public HomePath(String name) {
        this.name = name;
    }

    private HomePath(Parcel parcel) {
        super(parcel);
    }

    @Override
    public HomeScope withScope() {
        return new HomeScope(name);
    }

    @Override
    public View withView(Context context) {
        return new HomeView(context);
    }

    @Override
    protected void readParcel(Parcel parcel) {
        HomePathParcelablePlease.readFromParcel(this, parcel);
    }

    @Override
    protected void writeParcel(Parcel parcel) {
        HomePathParcelablePlease.writeToParcel(this, parcel, 0);
    }

    public static final Parcelable.Creator<HomePath> CREATOR = new Parcelable.Creator<HomePath>() {
        public HomePath createFromParcel(Parcel in) {
            return new HomePath(in);
        }

        public HomePath[] newArray(int size) {
            return new HomePath[size];
        }
    };
}
