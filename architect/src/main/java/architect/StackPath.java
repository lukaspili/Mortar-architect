package architect;

import android.content.Context;
import android.os.Parcel;
import android.view.View;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class StackPath<T extends StackScope> implements History.Entry.Factory {

    public StackPath() {

    }

    protected StackPath(Parcel parcel) {
        this();
        readParcel(parcel);
    }

    @Override
    public final StackScope createScope() {
        return withScope();
    }

    @Override
    public final View createView(Context context) {
        return withView(context);
    }

    @Override
    public final void writeToParcel(Parcel parcel, int i) {
        writeParcel(parcel);
    }

    @Override
    public final int describeContents() {
        return 0;
    }

    protected abstract T withScope();

    protected abstract View withView(Context context);

    protected abstract void readParcel(Parcel parcel);

    protected abstract void writeParcel(Parcel parcel);
}