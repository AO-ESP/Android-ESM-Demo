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
    private final String gtin;

    private final boolean EANblocked;

    @Nullable
    private final String message;

    public EanCheckResult(
            @NonNull String gtin,
            boolean EANblocked,
            @Nullable String message
    ) {
        this.gtin = gtin;
        this.EANblocked = EANblocked;
        this.message = message;
    }

    private EanCheckResult(Parcel in) {
        gtin = Objects.requireNonNull(in.readString());
        EANblocked = in.readInt() == 1;
        message = in.readInt() == 1 ? in.readString() : null;
    }

    @NonNull
    public String getGtin() {
        return gtin;
    }

    public boolean getEANblocked() {
        return EANblocked;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(gtin);
        dest.writeInt(EANblocked ? 1 : 0);
        if (message == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            dest.writeString(message);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EanCheckResult that)) return false;
        return EANblocked == that.EANblocked
                && Objects.equals(gtin, that.gtin)
                && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gtin, EANblocked, message);
    }

    @NonNull
    @Override
    public String toString() {
        return "EanCheckResult{" +
                "gtin='" + gtin + '\'' +
                ", EANblocked=" + EANblocked +
                ", message='" + message + '\'' +
                '}';
    }
}
