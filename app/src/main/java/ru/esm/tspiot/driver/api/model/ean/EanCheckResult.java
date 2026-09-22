package ru.esm.tspiot.driver.api.model.ean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

@Keep
public final class EanCheckResult implements Parcelable {

    @NonNull
    private final String itemId;
    @NonNull
    private final String gtin;
    private final boolean blocked;
    @Nullable
    private final Integer pg;

    public EanCheckResult(
            @NonNull String itemId,
            @NonNull String gtin,
            boolean blocked,
            @Nullable Integer pg
    ) {
        this.itemId = itemId;
        this.gtin = gtin;
        this.blocked = blocked;
        this.pg = pg;
    }

    private EanCheckResult(Parcel in) {
        itemId = Objects.requireNonNull(in.readString());
        gtin = Objects.requireNonNull(in.readString());
        blocked = in.readByte() != 0;
        pg = in.readByte() == 0 ? null : in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(itemId);
        dest.writeString(gtin);
        dest.writeByte((byte) (blocked ? 1 : 0));
        if (pg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(pg);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EanCheckResult> CREATOR = new Creator<>() {
        @Override
        public EanCheckResult createFromParcel(Parcel in) {
            return new EanCheckResult(in);
        }

        @Override
        public EanCheckResult[] newArray(int size) {
            return new EanCheckResult[size];
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

    public boolean getBlocked() {
        return blocked;
    }

    @Nullable
    public Integer getPg() {
        return pg;
    }

    @NonNull
    @Override
    public String toString() {
        return "EanCheckResult{" +
                "itemId='" + itemId + '\'' +
                ", gtin='" + gtin + '\'' +
                ", blocked=" + blocked +
                ", pg=" + pg +
                '}';
    }
}
