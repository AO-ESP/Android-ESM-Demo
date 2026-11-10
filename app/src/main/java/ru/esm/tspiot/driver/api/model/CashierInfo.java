package ru.esm.tspiot.driver.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import org.json.JSONException;
import org.json.JSONObject;

@Keep
public final class CashierInfo implements Parcelable {
    private final String info;
    private final String inn;

    public CashierInfo(String info, String inn) {
        this.info = info;
        this.inn = inn;
    }

    protected CashierInfo(Parcel in) {
        info = in.readString();
        inn = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(info);
        dest.writeString(inn);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CashierInfo> CREATOR = new Creator<CashierInfo>() {
        @Override
        public CashierInfo createFromParcel(Parcel in) {
            return new CashierInfo(in);
        }

        @Override
        public CashierInfo[] newArray(int size) {
            return new CashierInfo[size];
        }
    };

    public String getInfo() {
        return info;
    }

    public String getInn() {
        return inn;
    }

    @Override
    public String toString() {
        return "CashierInfo{" +
                "info='" + info + '\'' +
                ", inn='" + inn + '\'' +
                '}';
    }

    public static CashierInfo fromJson(String rawJson) {
        try {
            JSONObject object = new JSONObject(rawJson);
            String name = object.optString("info");
            String vatin = object.optString("inn");
            return new CashierInfo(name, vatin);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
