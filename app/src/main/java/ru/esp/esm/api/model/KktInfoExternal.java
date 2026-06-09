package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Objects;

@Keep
public final class KktInfoExternal implements Parcelable {
    private final String tsPiotId;
    private final String kktSerial;
    private final String fnSerial;
    private final String kktInn;
    private final Long codesCheckTimeout;
    private final LmInfoExternal lm;

    public KktInfoExternal(String tsPiotId, String kktSerial, String fnSerial,
                           String kktInn, Long codesCheckTimeout, LmInfoExternal lm) {
        this.tsPiotId = tsPiotId;
        this.kktSerial = kktSerial;
        this.fnSerial = fnSerial;
        this.kktInn = kktInn;
        this.codesCheckTimeout = codesCheckTimeout;
        this.lm = lm;
    }

    KktInfoExternal(Parcel in) {
        tsPiotId = in.readString();
        kktSerial = in.readString();
        fnSerial = in.readString();
        kktInn = in.readString();
        codesCheckTimeout = in.readLong();
        lm = in.readParcelable(LmInfoExternal.class.getClassLoader());
    }

    public static final Creator<KktInfoExternal> CREATOR = new Creator<KktInfoExternal>() {
        @Override
        public KktInfoExternal createFromParcel(Parcel in) {
            return new KktInfoExternal(in);
        }

        @Override
        public KktInfoExternal[] newArray(int size) {
            return new KktInfoExternal[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tsPiotId);
        dest.writeString(kktSerial);
        dest.writeString(fnSerial);
        dest.writeString(kktInn);
        dest.writeLong(codesCheckTimeout);
        dest.writeParcelable(lm, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof KktInfoExternal that)) return false;

        return Objects.equals(tsPiotId, that.tsPiotId) && Objects.equals(kktSerial, that.kktSerial)
                && Objects.equals(fnSerial, that.fnSerial) && Objects.equals(kktInn, that.kktInn)
                && Objects.equals(codesCheckTimeout, that.codesCheckTimeout)
                && Objects.equals(lm, that.lm);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(tsPiotId);
        result = 31 * result + Objects.hashCode(kktSerial);
        result = 31 * result + Objects.hashCode(fnSerial);
        result = 31 * result + Objects.hashCode(kktInn);
        result = 31 * result + Objects.hashCode(codesCheckTimeout);
        result = 31 * result + Objects.hashCode(lm);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "KktInfoExternal{" +
                "tspiotId='" + tsPiotId + '\'' +
                ", kktSerial='" + kktSerial + '\'' +
                ", fnSerial='" + fnSerial + '\'' +
                ", kktInn='" + kktInn + '\'' +
                ", codesCheckTimeout=" + codesCheckTimeout +
                ", lm=" + lm +
                '}';
    }
}
