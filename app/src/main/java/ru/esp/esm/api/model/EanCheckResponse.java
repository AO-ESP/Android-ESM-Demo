package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Keep
public final class EanCheckResponse implements Parcelable {

    @NonNull
    private final List<EanCheckResult> results;

    public EanCheckResponse(@NonNull List<EanCheckResult> results) {
        this.results = new ArrayList<>(results);
    }

    private EanCheckResponse(Parcel in) {
        results = Objects.requireNonNull(in.createTypedArrayList(EanCheckResult.CREATOR));
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(results);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EanCheckResponse> CREATOR = new Creator<>() {
        @Override
        public EanCheckResponse createFromParcel(Parcel in) {
            return new EanCheckResponse(in);
        }

        @Override
        public EanCheckResponse[] newArray(int size) {
            return new EanCheckResponse[size];
        }
    };

    @NonNull
    public List<EanCheckResult> getResults() {
        return results;
    }

    @NonNull
    @Override
    public String toString() {
        return "EanCheckResponse{" +
                "results=" + results +
                '}';
    }
}