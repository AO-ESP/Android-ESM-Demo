package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

@Keep
public final class KktInfoExternal implements Parcelable {
    private final String tsPiotId;
    private final String kktSerial;
    private final String fnSerial;
    private final String kktInn;
    private final Long codesCheckTimeout;
    private final LmInfoExternal lm;
    private final String licenceTill;

    public KktInfoExternal(String tsPiotId, String kktSerial, String fnSerial,
                           String kktInn, Long codesCheckTimeout, LmInfoExternal lm,
                           String licenceTill) {
        this.tsPiotId = tsPiotId;
        this.kktSerial = kktSerial;
        this.fnSerial = fnSerial;
        this.kktInn = kktInn;
        this.codesCheckTimeout = codesCheckTimeout;
        this.lm = lm;
        this.licenceTill = licenceTill;
    }

    KktInfoExternal(Parcel in) {
        tsPiotId = in.readString();
        kktSerial = in.readString();
        fnSerial = in.readString();
        kktInn = in.readString();
        codesCheckTimeout = in.readLong();
        lm = in.readParcelable(LmInfoExternal.class.getClassLoader());
        // Поле дописано в конец. Старый сервер его не передаёт — тогда в хвосте пусто.
        // Старый клиент останавливается на lm и не читает этот хвост.
        licenceTill = in.dataAvail() > 0 ? in.readString() : null;
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
        // Последним: старый клиент останавливается на lm и не читает это поле.
        dest.writeString(licenceTill);
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
                && Objects.equals(lm, that.lm)
                && Objects.equals(licenceTill, that.licenceTill);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(tsPiotId);
        result = 31 * result + Objects.hashCode(kktSerial);
        result = 31 * result + Objects.hashCode(fnSerial);
        result = 31 * result + Objects.hashCode(kktInn);
        result = 31 * result + Objects.hashCode(codesCheckTimeout);
        result = 31 * result + Objects.hashCode(lm);
        result = 31 * result + Objects.hashCode(licenceTill);
        return result;
    }

    @Nullable
    public String getLicenceTill() {
        return licenceTill;
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
                ", licenceTill='" + licenceTill + '\'' +
                '}';
    }
}
