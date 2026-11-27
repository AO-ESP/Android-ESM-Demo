package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Keep
public final class CodesCheckRequest implements Parcelable {
    /**
     * Маркер хвоста с {@code customerInn}, {@code mode} и {@code receiptType}.
     * Пишется только когда хотя бы одно из этих полей задано.
     */
    private static final int EAN_EXCLUSION_MARKER = 0x45414E31;

    @NonNull
    private final CisList cisList;
    @NonNull
    private final ClientInfo clientInfo;
    @Nullable
    private final String customerInn;

    public CodesCheckRequest(
            @NonNull CisList cisList,
            @NonNull ClientInfo clientInfo,
            @Nullable String customerInn
    ) {
        this.cisList = cisList;
        this.clientInfo = clientInfo;
        this.customerInn = customerInn;
    }

    public CodesCheckRequest(@NonNull CisList cisList, @NonNull ClientInfo clientInfo) {
        this(cisList, clientInfo, null);
    }

    private CodesCheckRequest(Parcel in) {
        CisList rawList = Objects.requireNonNull(in.readParcelable(CisList.class.getClassLoader()));
        clientInfo = Objects.requireNonNull(in.readParcelable(ClientInfo.class.getClassLoader()));
        String inn = null;
        if (in.dataAvail() >= 4) {
            int position = in.dataPosition();
            if (in.readInt() == EAN_EXCLUSION_MARKER) {
                String readInn = in.readString();
                List<Cis> source = rawList.getCodesList();
                if (in.dataAvail() < 4) {
                    in.setDataPosition(position);
                } else {
                    int count = in.readInt();
                    if (count != source.size()) {
                        in.setDataPosition(position);
                    } else {
                        List<Cis> rebuilt = new ArrayList<>(count);
                        for (int i = 0; i < count; i++) {
                            String mode = in.readString();
                            String receiptType = in.readString();
                            Cis item = source.get(i);
                            rebuilt.add(new Cis(item.getCis(), item.getPg(), mode, receiptType));
                        }
                        rawList = new CisList(rebuilt, rawList.getTz());
                        inn = readInn;
                    }
                }
            } else {
                in.setDataPosition(position);
            }
        }
        cisList = rawList;
        customerInn = inn;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(cisList, flags);
        dest.writeParcelable(clientInfo, flags);
        if (!hasEanExclusionTail()) {
            return;
        }
        dest.writeInt(EAN_EXCLUSION_MARKER);
        dest.writeString(customerInn);
        List<Cis> codes = cisList.getCodesList();
        dest.writeInt(codes.size());
        for (Cis item : codes) {
            dest.writeString(item.getMode());
            dest.writeString(item.getReceiptType());
        }
    }

    private boolean hasEanExclusionTail() {
        if (customerInn != null && !customerInn.isEmpty()) {
            return true;
        }
        for (Cis item : cisList.getCodesList()) {
            if (item.getMode() != null || item.getReceiptType() != null) {
                return true;
            }
        }
        return false;
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

    @Nullable
    public String getCustomerInn() {
        return customerInn;
    }

    @NonNull
    @Override
    public String toString() {
        return "CodesCheckRequest{" +
                "cisList=" + cisList +
                ", clientInfo=" + clientInfo +
                ", customerInn=" + customerInn +
                '}';
    }
}
