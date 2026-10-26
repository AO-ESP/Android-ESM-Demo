package ru.atol.os.tspiot.driver.api.model.ean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

@Keep
public final class GtinCheckError implements Parcelable {
    private final int code;
    private final String message;

    public GtinCheckError(int code, @NonNull String message) {
        this.code = code;
        this.message = message;
    }

    private GtinCheckError(Parcel in) {
        code = in.readInt();
        message = in.readString();
    }

    public int getCode() {
        return code;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GtinCheckError> CREATOR = new Creator<GtinCheckError>() {
        @Override
        public GtinCheckError createFromParcel(Parcel in) {
            return new GtinCheckError(in);
        }

        @Override
        public GtinCheckError[] newArray(int size) {
            return new GtinCheckError[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "GtinCheckError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
