package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Objects;

@Keep
public final class CodesCheckRequest implements Parcelable {
    @NonNull
    private final CisList cisList;
    @NonNull
    private final ClientInfo clientInfo;

    public CodesCheckRequest(@NonNull CisList cisList, @NonNull ClientInfo clientInfo) {
        this.cisList = cisList;
        this.clientInfo = clientInfo;
    }

    private CodesCheckRequest(Parcel in) {
        cisList = Objects.requireNonNull(in.readParcelable(CisList.class.getClassLoader()));
        clientInfo = Objects.requireNonNull(in.readParcelable(ClientInfo.class.getClassLoader()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(cisList, flags);
        dest.writeParcelable(clientInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CodesCheckRequest> CREATOR = new Creator<>() {
        @Override
        public CodesCheckRequest createFromParcel(Parcel in) {
            return new CodesCheckRequest(in);
        }

        @Override
        public CodesCheckRequest[] newArray(int size) {
            return new CodesCheckRequest[size];
        }
    };

    @NonNull
    public CisList getCisList() {
        return cisList;
    }

    @NonNull
    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    @NonNull
    @Override
    public String toString() {
        return "CodesCheckRequest{" +
                "cisList=" + cisList +
                ", clientInfo=" + clientInfo +
                '}';
    }
}
