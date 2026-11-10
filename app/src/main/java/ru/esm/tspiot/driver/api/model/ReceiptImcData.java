package ru.esm.tspiot.driver.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

@Keep
public final class ReceiptImcData implements Parcelable {
    // Старый клиент пишет только ki и ofdStatus. Следующий int в общем Parcel —
    // флаг элемента списка или KktInfo, он равен 0 или 1. Иначе readParcelable
    // принимает fnSerial за имя класса.
    private static final int GOODS_INFO_MARKER = 0x47494E46;

    @Nullable
    private final String ki;
    @Nullable
    private final String ofdStatus;
    @Nullable
    private final GoodsInfo goodsInfo;

    public ReceiptImcData(@Nullable String ki, @Nullable String ofdStatus) {
        this(ki, ofdStatus, null);
    }

    public ReceiptImcData(
            @Nullable String ki,
            @Nullable String ofdStatus,
            @Nullable GoodsInfo goodsInfo
    ) {
        this.ki = ki;
        this.ofdStatus = ofdStatus;
        this.goodsInfo = goodsInfo;
    }

    protected ReceiptImcData(Parcel in) {
        ki = in.readString();
        ofdStatus = in.readString();
        goodsInfo = readGoodsInfo(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ki);
        dest.writeString(ofdStatus);
        if (goodsInfo != null) {
            dest.writeInt(GOODS_INFO_MARKER);
            dest.writeParcelable(goodsInfo, flags);
        }
    }

    @Nullable
    private static GoodsInfo readGoodsInfo(Parcel in) {
        if (in.dataAvail() < 4) {
            return null;
        }
        int position = in.dataPosition();
        if (in.readInt() != GOODS_INFO_MARKER) {
            in.setDataPosition(position);
            return null;
        }
        return in.readParcelable(GoodsInfo.class.getClassLoader());
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

    @Nullable
    public String getKi() {
        return ki;
    }

    @Nullable
    public String getOfdStatus() {
        return ofdStatus;
    }

    @Nullable
    public GoodsInfo getGoodsInfo() {
        return goodsInfo;
    }

    @NonNull
    @Override
    public String toString() {
        return "ReceiptImcData{" +
                "ki='" + ki + '\'' +
                ", ofdStatus='" + ofdStatus + '\'' +
                ", goodsInfo=" + goodsInfo +
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
            String ki = nullableString(object, "ki");
            String ofdStatus = nullableString(object, "ofd_status");
            return new ReceiptImcData(ki, ofdStatus, parseGoodsInfo(object));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    private static String nullableString(JSONObject object, String key) {
        if (!object.has(key) || object.isNull(key)) {
            return null;
        }
        String value = object.optString(key, null);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value;
    }

    @Nullable
    private static GoodsInfo parseGoodsInfo(JSONObject object) throws JSONException {
        if (!object.has("goodsInfo") || object.isNull("goodsInfo")) {
            return null;
        }
        Object raw = object.get("goodsInfo");
        if (raw instanceof JSONObject) {
            return GoodsInfo.fromJson((JSONObject) raw);
        }
        if (raw instanceof String) {
            String json = (String) raw;
            if (json.isEmpty()) {
                return null;
            }
            return GoodsInfo.fromJson(new JSONObject(json));
        }
        return null;
    }
}
