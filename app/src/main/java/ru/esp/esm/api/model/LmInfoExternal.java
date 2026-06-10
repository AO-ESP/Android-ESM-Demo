package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Objects;

@Keep
public final class LmInfoExternal implements Parcelable {
    private final String version;
    private final String status;
    private final long lastSync;
    private final String token;
    private final String expDate;
    private final String ip;
    private final int port;
    private final String login;
    private final String pass;

    public LmInfoExternal(String version, String status, long lastSync, String token,
                          String expDate, String ip, int port, String login, String pass) {
        this.version = version;
        this.status = status;
        this.lastSync = lastSync;
        this.token = token;
        this.expDate = expDate;
        this.ip = ip;
        this.port = port;
        this.login = login;
        this.pass = pass;
    }

    LmInfoExternal(Parcel in) {
        version = in.readString();
        status = in.readString();
        lastSync = in.readLong();
        token = in.readString();
        expDate = in.readString();
        ip = in.readString();
        port = in.readInt();
        login = in.readString();
        pass = in.readString();
    }

    public static final Creator<LmInfoExternal> CREATOR = new Creator<LmInfoExternal>() {
        @Override
        public LmInfoExternal createFromParcel(Parcel in) {
            return new LmInfoExternal(in);
        }

        @Override
        public LmInfoExternal[] newArray(int size) {
            return new LmInfoExternal[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(version);
        dest.writeString(status);
        dest.writeLong(lastSync);
        dest.writeString(token);
        dest.writeString(expDate);
        dest.writeString(ip);
        dest.writeInt(port);
        dest.writeString(login);
        dest.writeString(pass);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LmInfoExternal that)) return false;

        return lastSync == that.lastSync && port == that.port && Objects.equals(version, that.version)
                && Objects.equals(status, that.status) && Objects.equals(token, that.token)
                && Objects.equals(expDate, that.expDate) && Objects.equals(ip, that.ip)
                && Objects.equals(login, that.login) && Objects.equals(pass, that.pass);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(version);
        result = 31 * result + Objects.hashCode(status);
        result = 31 * result + Long.hashCode(lastSync);
        result = 31 * result + Objects.hashCode(token);
        result = 31 * result + Objects.hashCode(expDate);
        result = 31 * result + Objects.hashCode(ip);
        result = 31 * result + port;
        result = 31 * result + Objects.hashCode(login);
        result = 31 * result + Objects.hashCode(pass);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "LmInfoExternal{" +
                "version='" + version + '\'' +
                ", status='" + status + '\'' +
                ", lastSync=" + lastSync +
                ", token='" + token + '\'' +
                ", expDate='" + expDate + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
