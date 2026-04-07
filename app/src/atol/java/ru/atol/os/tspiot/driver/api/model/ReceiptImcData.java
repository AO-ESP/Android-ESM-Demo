package ru.atol.os.tspiot.driver.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

@Keep
public final class ReceiptImcData implements Parcelable {
    private final String ki;
    private final String ofdStatus;

    public ReceiptImcData(String ki, String ofdStatus) {
        this.ki = ki;
        this.ofdStatus = ofdStatus;
    }

    protected ReceiptImcData(Parcel in) {
        ki = in.readString();
        ofdStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ki);
        dest.writeString(ofdStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReceiptImcData> CREATOR = new Creator<ReceiptImcData>() {
        @Override
        public ReceiptImcData createFromParcel(Parcel in) {
            return new ReceiptImcData(in);
        }

        @Override
        public ReceiptImcData[] newArray(int size) {
            return new ReceiptImcData[size];
        }
    };

    public String getKi() {
        return ki;
    }

    public String getOfdStatus() {
        return ofdStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return "ReceiptImcData{" +
                "ki='" + ki + '\'' +
                ", ofdStatus='" + ofdStatus + '\'' +
                '}';
    }

    public static List<ReceiptImcData> fromJsonArray(String jsonArray) {
        try {
            List<ReceiptImcData> result = new LinkedList<>();
            JSONArray array = new JSONArray(jsonArray);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                result.add(fromJson(object.toString()));
            }
            return result;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public static ReceiptImcData fromJson(String jsonObject) {
        try {
            JSONObject object = new JSONObject(jsonObject);
            String ki = object.optString("ki");
            String ofdStatus = object.optString("ofd_status");
            return new ReceiptImcData(ki, ofdStatus);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
