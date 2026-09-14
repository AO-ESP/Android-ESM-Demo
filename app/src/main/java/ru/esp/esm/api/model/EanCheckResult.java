package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

@Keep
public final class EanCheckResult implements Parcelable {

    @NonNull
    private final String ean;
    @Nullable
    private final Boolean found;
    @Nullable
    private final Boolean blocked;
    @Nullable
    private final Integer pg;

    public EanCheckResult(
            @NonNull String ean,
            @Nullable Boolean found,
            @Nullable Boolean blocked,
            @Nullable Integer pg
    ) {
        this.ean = ean;
        this.found = found;
        this.blocked = blocked;
        this.pg = pg;
    }

    private EanCheckResult(Parcel in) {
        ean = Objects.requireNonNull(in.readString());
        found = in.readByte() == 0 ? null : in.readByte() != 0;
        blocked = in.readByte() == 0 ? null : in.readByte() != 0;
        pg = in.readByte() == 0 ? null : in.readInt();
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(ean);
        writeNullableBoolean(dest, found);
        writeNullableBoolean(dest, blocked);
        if (pg == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(pg);
        }
    }

    private static void writeNullableBoolean(@NonNull Parcel dest, @Nullable Boolean value) {
        if (value == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeByte((byte) (value ? 1 : 0));
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
    public String getEan() {
        return ean;
    }

    @Nullable
    public Boolean getFound() {
        return found;
    }

    @Nullable
    public Boolean getBlocked() {
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
                "ean='" + ean + '\'' +
                ", found=" + found +
                ", blocked=" + blocked +
                ", pg=" + pg +
                '}';
    }
}