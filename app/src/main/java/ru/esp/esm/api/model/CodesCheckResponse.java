package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Keep
public final class CodesCheckResponse implements Parcelable {
    private final int code;

    @NonNull
    private final String description;
    @NonNull
    private final List<MarkingCodeInfo> codes;
    @NonNull
    private final String reqId;

    private final long reqTimestamp;

    private final boolean isCheckedOffline;

    @Nullable
    private final String inst;

    @Nullable
    private final String version;
    @NonNull
    private final List<EanCheckResult> eans;

    public CodesCheckResponse(
            int code,
            @NonNull String description,
            @NonNull List<MarkingCodeInfo> codes,
            @NonNull String reqId,
            long reqTimestamp,
            boolean isCheckedOffline,
            @Nullable String inst,
            @Nullable String version,
            @NonNull List<EanCheckResult> eans
    ) {
        this.code = code;
        this.description = description;
        this.codes = codes;
        this.reqId = reqId;
        this.reqTimestamp = reqTimestamp;
        this.isCheckedOffline = isCheckedOffline;
        this.inst = inst;
        this.version = version;
        this.eans = new ArrayList<>(eans);
    }

    public CodesCheckResponse(
            int code,
            @NonNull String description,
            @NonNull List<MarkingCodeInfo> codes,
            @NonNull String reqId,
            long reqTimestamp,
            boolean isCheckedOffline,
            @Nullable String inst,
            @Nullable String version
    ) {
        this(code, description, codes, reqId, reqTimestamp, isCheckedOffline, inst, version, Collections.emptyList());
    }

    public CodesCheckResponse(
            int code,
            @NonNull String description,
            @NonNull List<MarkingCodeInfo> codes,
            @NonNull String reqId,
            long reqTimestamp,
            boolean isCheckedOffline
    ) {
        this(code, description, codes, reqId, reqTimestamp, isCheckedOffline, null, null, Collections.emptyList());
    }

    CodesCheckResponse(Parcel in) {
        code = in.readInt();
        description = Objects.requireNonNull(in.readString());
        codes = Objects.requireNonNull(in.createTypedArrayList(MarkingCodeInfo.CREATOR));
        reqId = Objects.requireNonNull(in.readString());
        reqTimestamp = in.readLong();
        isCheckedOffline = in.readByte() != 0;
        inst = in.readByte() == 0 ? null : in.readString();
        version = in.readByte() == 0 ? null : in.readString();
        List<EanCheckResult> parcelEans = in.createTypedArrayList(EanCheckResult.CREATOR);
        eans = parcelEans != null ? parcelEans : Collections.emptyList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(description);
        dest.writeTypedList(codes);
        dest.writeString(reqId);
        dest.writeLong(reqTimestamp);
        dest.writeByte((byte) (isCheckedOffline ? 1 : 0));
        dest.writeByte((byte) (inst == null ? 0 : 1));
        if (inst != null) {
            dest.writeString(inst);
        }
        dest.writeByte((byte) (version == null ? 0 : 1));
        if (version != null) {
            dest.writeString(version);
        }
        dest.writeTypedList(eans);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CodesCheckResponse> CREATOR = new Creator<>() {
        @Override
        public CodesCheckResponse createFromParcel(Parcel in) {
            return new CodesCheckResponse(in);
        }

        @Override
        public CodesCheckResponse[] newArray(int size) {
            return new CodesCheckResponse[size];
        }
    };

    public int getCode() {
        return code;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public List<MarkingCodeInfo> getCodes() {
        return codes;
    }

    @NonNull
    public String getReqId() {
        return reqId;
    }

    public long getReqTimestamp() {
        return reqTimestamp;
    }

    public boolean isCheckedOffline() {
        return isCheckedOffline;
    }

    @Nullable
    public String getInst() {
        return inst;
    }

    @Nullable
    public String getVersion() {
        return version;
    }

    @NonNull
    public List<EanCheckResult> getEans() {
        return eans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodesCheckResponse that)) return false;
        return code == that.code && reqTimestamp == that.reqTimestamp &&
                Objects.equals(description, that.description) && isCheckedOffline == that.isCheckedOffline &&
                Objects.equals(codes, that.codes) && Objects.equals(reqId, that.reqId) &&
                Objects.equals(inst, that.inst) && Objects.equals(version, that.version) &&
                Objects.equals(eans, that.eans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, description, codes, reqId, reqTimestamp, isCheckedOffline, inst, version, eans);
    }

    @NonNull
    @Override
    public String toString() {
        return "CodesCheckResponse{" +
                "code=" + code +
                ", description='" + description + '\'' +
                ", codes=" + codes +
                ", reqId='" + reqId + '\'' +
                ", reqTimestamp=" + reqTimestamp +
                ", isCheckedOffline=" + isCheckedOffline +
                ", inst='" + inst + '\'' +
                ", version='" + version + '\'' +
                ", eans=" + eans +
                '}';
    }
}
