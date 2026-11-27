package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Код идентификации.
 * <p>
 * {@code mode} и {@code receiptType} в parcel самого {@code Cis} не пишутся:
 * их кладет хвост {@link CodesCheckRequest}, иначе старый список кодов разъедется.
 */
public class Cis implements Parcelable {
    @NonNull
    private final String cis;
    @Nullable
    private final Integer pg;
    @Nullable
    private final String mode;
    @Nullable
    private final String receiptType;

    public Cis(
            @NonNull String cis,
            @Nullable Integer pg,
            @Nullable String mode,
            @Nullable String receiptType
    ) {
        this.cis = cis;
        this.pg = pg;
        this.mode = mode;
        this.receiptType = receiptType;
    }

    public Cis(@NonNull String cis, @Nullable Integer pg) {
        this(cis, pg, null, null);
    }

    public Cis(@NonNull String cis) {
        this(cis, null, null, null);
    }

    protected Cis(@NonNull Parcel in) {
        this.cis = Objects.requireNonNull(in.readString());
        this.pg = in.readByte() == 0 ? null : in.readInt();
        this.mode = null;
        this.receiptType = null;
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

    @Nullable
    public String getMode() {
        return mode;
    }

    @Nullable
    public String getReceiptType() {
        return receiptType;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cis{" +
                "cis='" + cis + '\'' +
                ", pg=" + pg +
                ", mode=" + mode +
                ", receiptType=" + receiptType +
                '}';
    }
}
