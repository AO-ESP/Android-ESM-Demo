package ru.atol.os.tspiot.driver.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public final class ErrorRequest implements Parcelable {
    private final List<ErrorInfo> errors;

    public ErrorRequest(List<ErrorInfo> errors) {
        this.errors = errors;
    }

    private ErrorRequest(Parcel in) {
        errors = in.createTypedArrayList(ErrorInfo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(errors);
    }

    public List<ErrorInfo> getErrors() {
        return errors;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ErrorRequest> CREATOR = new Creator<ErrorRequest>() {
        @Override
        public ErrorRequest createFromParcel(Parcel in) {
            return new ErrorRequest(in);
        }

        @Override
        public ErrorRequest[] newArray(int size) {
            return new ErrorRequest[size];
        }
    };
}
