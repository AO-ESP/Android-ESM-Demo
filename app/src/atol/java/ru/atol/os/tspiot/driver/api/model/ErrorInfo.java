package ru.atol.os.tspiot.driver.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

@Keep
public final class ErrorInfo implements Parcelable {
    public static final String ERROR_TYPE = "error";
    public static final String INFO_TYPE = "info";
    public static final String WARNING_TYPE = "warning";
    public static final String DEBUG_TYPE = "debug";

    private final int code;
    private final String message;
    private final String module;
    private final String type;

    public ErrorInfo(int code, String message, String module, String type) {
        this.code = code;
        this.message = message;
        this.module = module;
        this.type = type;
    }

    private ErrorInfo(Parcel in) {
        code = in.readInt();
        message = in.readString();
        module = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(message);
        dest.writeString(module);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ErrorInfo> CREATOR = new Creator<ErrorInfo>() {
        @Override
        public ErrorInfo createFromParcel(Parcel in) {
            return new ErrorInfo(in);
        }

        @Override
        public ErrorInfo[] newArray(int size) {
            return new ErrorInfo[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getModule() {
        return module;
    }
}
