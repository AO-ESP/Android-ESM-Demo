package ru.esm.tspiot.driver.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public final class KktInfo implements Parcelable {
    private final String fnSerial;
    private final String kktInn;
    private final String kktSerial;
    private final String firmwareVersion;
    private final String fnVersion;

    public KktInfo(String fnSerial, String kktInn, String kktSerial, String firmwareVersion, String fnVersion) {
        this.fnSerial = fnSerial;
        this.kktInn = kktInn;
        this.kktSerial = kktSerial;
        this.firmwareVersion = firmwareVersion;
        this.fnVersion = fnVersion;
    }

    protected KktInfo(Parcel in) {
        fnSerial = in.readString();
        kktInn = in.readString();
        kktSerial = in.readString();
        firmwareVersion = in.readString();
        fnVersion = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fnSerial);
        dest.writeString(kktInn);
        dest.writeString(kktSerial);
        dest.writeString(firmwareVersion);
        dest.writeString(fnVersion);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<KktInfo> CREATOR = new Creator<KktInfo>() {
        @Override
        public KktInfo createFromParcel(Parcel in) {
            return new KktInfo(in);
        }

        @Override
        public KktInfo[] newArray(int size) {
            return new KktInfo[size];
        }
    };

    @Override
    public String toString() {
        return "KktInfo{" +
                "fnSerial='" + fnSerial + '\'' +
                ", kktInn='" + kktInn + '\'' +
                ", kktSerial='" + kktSerial + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", fnVersion='" + fnVersion + '\'' +
                '}';
    }

    public String getFnSerial() {
        return fnSerial;
    }

    public String getKktInn() {
        return kktInn;
    }

    public String getKktSerial() {
        return kktSerial;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getFnVersion() {
        return fnVersion;
    }
}