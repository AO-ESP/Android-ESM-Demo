package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Код идентификации
 */
public class Cis implements Parcelable {
    @NonNull
    private final String cis;
    @Nullable
    private final Integer pg;

    public Cis(@NonNull String cis, @Nullable Integer pg) {
        this.cis = cis;
        this.pg = pg;
    }

    public Cis(@NonNull String cis) {
        this.cis = cis;
        this.pg = null;
    }

    protected Cis(@NonNull Parcel in) {
        this.cis = Objects.requireNonNull(in.readString());
        this.pg = in.readByte() == 0 ? null : in.readInt();
    }


    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(cis);
        dest.writeByte((byte) (pg == null ? 0 : 1));
        if (pg != null) {
            dest.writeInt(pg);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Cis> CREATOR = new Creator<Cis>() {
        @Override
        public Cis createFromParcel(@NonNull Parcel in) {
            return new Cis(in);
        }

        @Override
        public Cis[] newArray(int size) {
            return new Cis[size];
        }
    };

    @NonNull
    public String getCis() {
        return cis;
    }

    @Nullable
    public Integer getPg() {
        return pg;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cis{" +
                "cis='" + cis + '\'' +
                ", pg=" + pg +
                '}';
    }
}