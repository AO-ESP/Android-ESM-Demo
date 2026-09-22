package ru.esm.tspiot.driver.api.model.ean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Objects;

@Keep
public final class EanCheckItem implements Parcelable {

    @NonNull
    private final String itemId;
    @NonNull
    private final String gtin;

    public EanCheckItem(@NonNull String itemId, @NonNull String gtin) {
        this.itemId = itemId;
        this.gtin = gtin;
    }

    private EanCheckItem(Parcel in) {
        itemId = Objects.requireNonNull(in.readString());
        gtin = Objects.requireNonNull(in.readString());
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(itemId);
        dest.writeString(gtin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EanCheckItem> CREATOR = new Creator<>() {
        @Override
        public EanCheckItem createFromParcel(Parcel in) {
            return new EanCheckItem(in);
        }

        @Override
        public EanCheckItem[] newArray(int size) {
            return new EanCheckItem[size];
        }
    };

    @NonNull
    public String getItemId() {
        return itemId;
    }

    @NonNull
    public String getGtin() {
        return gtin;
    }

    @NonNull
    @Override
    public String toString() {
        return "EanCheckItem{" +
                "itemId='" + itemId + '\'' +
                ", gtin='" + gtin + '\'' +
                '}';
    }
}
