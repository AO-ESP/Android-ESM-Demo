package ru.atol.os.tspiot.driver.api.model.ean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

@Keep
public final class EanCheckRequest implements Parcelable {
    private final List<EanCheckItem> items;

    public EanCheckRequest(@Nullable List<EanCheckItem> items) {
        this.items = items;
    }

    private EanCheckRequest(Parcel in) {
        items = in.createTypedArrayList(EanCheckItem.CREATOR);
    }

    @Nullable
    public List<EanCheckItem> getItems() {
        return items;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EanCheckRequest> CREATOR = new Creator<EanCheckRequest>() {
        @Override
        public EanCheckRequest createFromParcel(Parcel in) {
            return new EanCheckRequest(in);
        }

        @Override
        public EanCheckRequest[] newArray(int size) {
            return new EanCheckRequest[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "EanCheckRequest{" +
                "items=" + items +
                '}';
    }
}
