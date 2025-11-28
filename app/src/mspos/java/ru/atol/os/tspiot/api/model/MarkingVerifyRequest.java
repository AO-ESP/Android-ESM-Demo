package ru.atol.os.tspiot.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

@Keep
public final class MarkingVerifyRequest implements Parcelable {

    @NonNull
    private final List<String> codes;

    @Nullable
    private final String inn;

    @Nullable
    private final String fnNumber;

    @NonNull
    private final ClientInfo clientInfo;

    public MarkingVerifyRequest(@NonNull List<String> codes, @NonNull ClientInfo clientInfo) {
        this.codes = codes;
        this.inn = null;
        this.fnNumber = null;
        this.clientInfo = clientInfo;
    }

    public MarkingVerifyRequest(
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

    private MarkingVerifyRequest(Parcel in) {
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

    public static final Creator<MarkingVerifyRequest> CREATOR = new Creator<MarkingVerifyRequest>() {
        @Override
        public MarkingVerifyRequest createFromParcel(Parcel in) {
            return new MarkingVerifyRequest(in);
        }

        @Override
        public MarkingVerifyRequest[] newArray(int size) {
            return new MarkingVerifyRequest[size];
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
        return "MarkingVerifyRequest{" +
                "codes=" + codes +
                ", inn='" + inn + '\'' +
                ", fnNumber='" + fnNumber + '\'' +
                ", clientInfo=" + clientInfo +
                ", clientInfo=" + clientInfo +
                '}';
    }
}
