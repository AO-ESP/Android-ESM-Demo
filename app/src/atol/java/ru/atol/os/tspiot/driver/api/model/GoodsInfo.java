package ru.atol.os.tspiot.driver.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.json.JSONObject;

@Keep
public final class GoodsInfo implements Parcelable {
    private static final String MISSING_STRING = "UNKNOWN";
    private static final int MISSING_NUMBER = -1;

    private final String goodsName;
    private final double price;
    private final String scannedCode;
    private final double quantity;
    private final int paymentType;
    private final int itemType;
    private final int unitMeasurementType;
    private final int goodsInfoCheckResult;
    private final int taxRate;
    private final double amount;

    public GoodsInfo(
            @Nullable String goodsName,
            double price,
            @Nullable String scannedCode,
            double quantity,
            int paymentType,
            int itemType,
            int unitMeasurementType,
            int goodsInfoCheckResult,
            int taxRate,
            double amount
    ) {
        this.goodsName = stringOrUnknown(goodsName);
        this.price = numberOrMissing(price);
        this.scannedCode = stringOrUnknown(scannedCode);
        this.quantity = numberOrMissing(quantity);
        this.paymentType = paymentType;
        this.itemType = itemType;
        this.unitMeasurementType = unitMeasurementType;
        this.goodsInfoCheckResult = goodsInfoCheckResult;
        this.taxRate = taxRate;
        this.amount = numberOrMissing(amount);
    }

    private GoodsInfo(@NonNull Parcel in) {
        this(
                in.readString(),
                in.readDouble(),
                in.readString(),
                in.readDouble(),
                in.readInt(),
                in.readInt(),
                in.readInt(),
                in.readInt(),
                in.readInt(),
                in.readDouble()
        );
    }

    @NonNull
    public String getGoodsName() {
        return goodsName;
    }

    public double getPrice() {
        return price;
    }

    @NonNull
    public String getScannedCode() {
        return scannedCode;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public int getItemType() {
        return itemType;
    }

    public int getUnitMeasurementType() {
        return unitMeasurementType;
    }

    public int getGoodsInfoCheckResult() {
        return goodsInfoCheckResult;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(goodsName);
        dest.writeDouble(price);
        dest.writeString(scannedCode);
        dest.writeDouble(quantity);
        dest.writeInt(paymentType);
        dest.writeInt(itemType);
        dest.writeInt(unitMeasurementType);
        dest.writeInt(goodsInfoCheckResult);
        dest.writeInt(taxRate);
        dest.writeDouble(amount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GoodsInfo> CREATOR = new Creator<GoodsInfo>() {
        @Override
        public GoodsInfo createFromParcel(Parcel in) {
            return new GoodsInfo(in);
        }

        @Override
        public GoodsInfo[] newArray(int size) {
            return new GoodsInfo[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "GoodsInfo{" +
                "goodsName='" + goodsName + '\'' +
                ", price=" + price +
                ", scannedCode='" + scannedCode + '\'' +
                ", quantity=" + quantity +
                ", paymentType=" + paymentType +
                ", itemType=" + itemType +
                ", unitMeasurementType=" + unitMeasurementType +
                ", goodsInfoCheckResult=" + goodsInfoCheckResult +
                ", taxRate=" + taxRate +
                ", amount=" + amount +
                '}';
    }

    @NonNull
    public static GoodsInfo fromJson(@NonNull JSONObject object) {
        return new GoodsInfo(
                jsonString(object, "goodsName"),
                jsonDouble(object, "price"),
                jsonString(object, "scannedCode"),
                jsonDouble(object, "quantity"),
                jsonInt(object, "paymentType"),
                jsonInt(object, "itemType"),
                jsonInt(object, "unitMeasurementType"),
                jsonInt(object, "goodsInfoCheckResult"),
                jsonInt(object, "taxRate"),
                jsonDouble(object, "amount")
        );
    }

    @NonNull
    private static String stringOrUnknown(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return MISSING_STRING;
        }
        return value;
    }

    private static double numberOrMissing(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return MISSING_NUMBER;
        }
        return value;
    }

    @Nullable
    private static String jsonString(@NonNull JSONObject object, @NonNull String key) {
        if (!object.has(key) || object.isNull(key)) {
            return null;
        }
        String value = object.optString(key, null);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value;
    }

    private static int jsonInt(@NonNull JSONObject object, @NonNull String key) {
        if (!object.has(key) || object.isNull(key)) {
            return MISSING_NUMBER;
        }
        return object.optInt(key, MISSING_NUMBER);
    }

    private static double jsonDouble(@NonNull JSONObject object, @NonNull String key) {
        if (!object.has(key) || object.isNull(key)) {
            return MISSING_NUMBER;
        }
        return object.optDouble(key, MISSING_NUMBER);
    }
}
