package ru.atol.os.tspiot.driver.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public final class ReceiptInfo implements Parcelable {
    private final String receiptId;
    private final List<ReceiptImcData> imcData;

    public ReceiptInfo(String receiptId, List<ReceiptImcData> imcData) {
        this.receiptId = receiptId;
        this.imcData = imcData;
    }

    protected ReceiptInfo(Parcel in) {
        receiptId = in.readString();
        imcData = in.createTypedArrayList(ReceiptImcData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(receiptId);
        dest.writeTypedList(imcData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReceiptInfo> CREATOR = new Creator<ReceiptInfo>() {
        @Override
        public ReceiptInfo createFromParcel(Parcel in) {
            return new ReceiptInfo(in);
        }

        @Override
        public ReceiptInfo[] newArray(int size) {
            return new ReceiptInfo[size];
        }
    };

    public String getReceiptId() {
        return receiptId;
    }

    public List<ReceiptImcData> getImcData() {
        return imcData;
    }
}
