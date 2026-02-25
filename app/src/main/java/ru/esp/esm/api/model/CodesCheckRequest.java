package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

@Keep
public final class CodesCheckRequest implements Parcelable {

    @NonNull
    private final List<String> codes;

    @Nullable
    private final String inn;

    @Nullable
    private final String fnNumber;

    @NonNull
    private final ClientInfo clientInfo;

    public CodesCheckRequest(@NonNull List<String> codes, @NonNull ClientInfo clientInfo) {
        this.codes = codes;
        this.inn = null;
        this.fnNumber = null;
        this.clientInfo = clientInfo;
    }

    public CodesCheckRequest(
            @NonNull List<String> codes,
            @Nullable String inn,
            @Nullable String fnNumber,
            @NonNull ClientInfo clientInfo
    ) {
        this.codes = codes;
        this.inn = inn;
        this.fnNumber = fnNumber;
        this.clientInfo = clientInfo;
    }

    private CodesCheckRequest(Parcel in) {
        codes = Objects.requireNonNull(in.createStringArrayList());
        inn = in.readString();
        fnNumber = in.readString();
        clientInfo = Objects.requireNonNull(in.readParcelable(ClientInfo.class.getClassLoader()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(codes);
        dest.writeString(inn);
        dest.writeString(fnNumber);
        dest.writeParcelable(clientInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CodesCheckRequest> CREATOR = new Creator<CodesCheckRequest>() {
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
    public List<String> getCodes() {
        return codes;
    }

    @Nullable
    public String getFnNumber() {
        return fnNumber;
    }

    @Nullable
    public String getInn() {
        return inn;
    }

    @NonNull
    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    @NonNull
    @Override
    public String toString() {
        return "CodesCheckRequest{" +
                "codes=" + codes +
                ", inn='" + inn + '\'' +
                ", fnNumber='" + fnNumber + '\'' +
                ", clientInfo=" + clientInfo +
                ", clientInfo=" + clientInfo +
                '}';
    }
}
