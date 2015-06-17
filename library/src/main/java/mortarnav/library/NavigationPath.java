package mortarnav.library;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class NavigationPath<T extends NavigationScope> implements Parcelable {

    public NavigationPath() {

    }

    protected NavigationPath(Parcel parcel) {
        this();
        readParcel(parcel);
    }

    @Override
    public final void writeToParcel(Parcel parcel, int i) {
        writeParcel(parcel);
    }

    @Override
    public final int describeContents() {
        return 0;
    }

    public abstract T withScope();

    public abstract View withView(Context context);

    protected abstract void readParcel(Parcel parcel);

    protected abstract void writeParcel(Parcel parcel);
}