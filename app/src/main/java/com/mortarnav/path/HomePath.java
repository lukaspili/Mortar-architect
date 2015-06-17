package com.mortarnav.path;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.mortarnav.nav.HomeScope;
import com.mortarnav.view.HomeView;

import mortarnav.library.NavigationPath;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class HomePath extends NavigationPath<HomeScope> {

    private String name;

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
        name = parcel.readString();
    }

    @Override
    protected void writeParcel(Parcel parcel) {
        parcel.writeString(name);
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
