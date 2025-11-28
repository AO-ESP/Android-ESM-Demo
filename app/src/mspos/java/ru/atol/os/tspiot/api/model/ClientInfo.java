package ru.atol.os.tspiot.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Objects;

@Keep
public final class ClientInfo implements Parcelable {

    @NonNull
    private final String name;

    @NonNull
    private final String version;

    @NonNull
    private final String id;

    @NonNull
    private final String token;

    public ClientInfo(
            @NonNull String name,
            @NonNull String version,
            @NonNull String id,
            @NonNull String token
    ) {
        this.name = name;
        this.version = version;
        this.id = id;
        this.token = token;
    }

    ClientInfo(Parcel in) {
        name = Objects.requireNonNull(in.readString());
        version = Objects.requireNonNull(in.readString());
        id = Objects.requireNonNull(in.readString());
        token = Objects.requireNonNull(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(version);
        dest.writeString(id);
        dest.writeString(token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClientInfo> CREATOR = new Creator<ClientInfo>() {

        @Override
        public ClientInfo createFromParcel(Parcel in) {
            return new ClientInfo(in);
        }

        @Override
        public ClientInfo[] newArray(int size) {
            return new ClientInfo[size];
        }
    };

    @NonNull
    public String getClientName() {
        return name;
    }

    @NonNull
    public String getClientVersion() {
        return version;
    }

    @NonNull
    public String getClientId() {
        return id;
    }

    @NonNull
    public String getClientToken() {
        return token;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ClientInfo)) return false;
        ClientInfo that = (ClientInfo) object;

        return name.equals(that.name) && version.equals(that.version) && id.equals(that.id) && token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version, id, version);
    }

    @NonNull
    @Override
    public String toString() {
        return "ClientInfo{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", id='" + id + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}