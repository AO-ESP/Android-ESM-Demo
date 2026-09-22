package ru.esm.tspiot.driver.api.model.ean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Keep
public final class EanCheckRequest implements Parcelable {

    @NonNull
    private final List<EanCheckItem> items;

    public EanCheckRequest(@NonNull List<EanCheckItem> items) {
        this.items = new ArrayList<>(items);
    }

    private EanCheckRequest(Parcel in) {
        items = Objects.requireNonNull(in.createTypedArrayList(EanCheckItem.CREATOR));
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EanCheckRequest> CREATOR = new Creator<>() {
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
    public List<EanCheckItem> getItems() {
        return items;
    }

    @NonNull
    @Override
    public String toString() {
        return "EanCheckRequest{" +
                "items=" + items +
                '}';
    }
}
