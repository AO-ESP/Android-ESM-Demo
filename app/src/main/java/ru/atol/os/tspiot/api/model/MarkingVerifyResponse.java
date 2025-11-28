package ru.atol.os.tspiot.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Keep
public final class MarkingVerifyResponse implements Parcelable {

    @NonNull
    private final List<MarkingCodeInfo> codes;

    private final long reqTimestamp;

    @NonNull
    private final String reqId;

    @Nullable
    private final String inst;

    @Nullable
    private final String version;

    public MarkingVerifyResponse(
            @NonNull List<MarkingCodeInfo> codes,
            long reqTimestamp,
            @NonNull String reqId,
            @Nullable String inst,
            @Nullable String version
    ) {
        this.codes = codes;
        this.reqTimestamp = reqTimestamp;
        this.reqId = reqId;
        this.inst = inst;
        this.version = version;
    }

    MarkingVerifyResponse(Parcel in) {
        codes = in.createTypedArrayList(MarkingCodeInfo.CREATOR);
        reqTimestamp = in.readLong();
        reqId = in.readString();
        inst = in.readString();
        version = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(codes);
        dest.writeLong(reqTimestamp);
        dest.writeString(reqId);
        dest.writeString(inst);
        dest.writeString(version);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarkingVerifyResponse> CREATOR = new Creator<MarkingVerifyResponse>() {
        @Override
        public MarkingVerifyResponse createFromParcel(Parcel in) {
            return new MarkingVerifyResponse(in);
        }

        @Override
        public MarkingVerifyResponse[] newArray(int size) {
            return new MarkingVerifyResponse[size];
        }
    };

    public long getReqTimestamp() {
        return reqTimestamp;
    }

    @NonNull
    public String getReqId() {
        return reqId;
    }

    @NonNull
    public List<MarkingCodeInfo> getCodes() {
        return codes;
    }

    @Nullable
    public String getInst() {
        return inst;
    }

    @Nullable
    public String getVersion() {
        return version;
    }

//    /**
//     * @see MarkingCodeInfoMapper#fillOfflineWithDefaults(MarkingCodeInfo)
//     */
//    @NonNull
//    public List<MarkingCodeInfo> getCodesWithDefaults() {
//        return codes.stream().map(MarkingCodeInfoMapper::fillOfflineWithDefaults)
//                .collect(Collectors.toList());
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarkingVerifyResponse)) return false;
        MarkingVerifyResponse that = (MarkingVerifyResponse) o;
        return reqTimestamp == that.reqTimestamp && Objects.equals(codes, that.codes) &&
                Objects.equals(reqId, that.reqId) && Objects.equals(inst, that.inst) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codes, reqTimestamp, reqId, inst, version);
    }

    @Override
    public String toString() {
        return "MarkingVerifyResponse{" +
                "codes=" + codes +
                ", reqTimestamp=" + reqTimestamp +
                ", reqId='" + reqId + '\'' +
                ", inst='" + inst + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
