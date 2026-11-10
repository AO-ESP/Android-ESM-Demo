package ru.atol.os.tspiot.driver.api.model.ean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@Keep
public final class EanCheckResult implements Parcelable {
    private final String itemId;
    private final String gtin;
    private final boolean blocked;
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
        itemId = in.readString();
        gtin = in.readString();
        blocked = in.readInt() == 1;
        pg = in.readInt() == 1 ? in.readInt() : null;
    }

    @NonNull
    public String getItemId() {
        return itemId;
    }

    @NonNull
    public String getGtin() {
        return gtin;
    }

    public boolean isBlocked() {
        return blocked;
    }

    @Nullable
    public Integer getPg() {
        return pg;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(itemId);
        dest.writeString(gtin);
        dest.writeInt(blocked ? 1 : 0);
        if (pg == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            dest.writeInt(pg);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EanCheckResult> CREATOR = new Creator<EanCheckResult>() {
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
