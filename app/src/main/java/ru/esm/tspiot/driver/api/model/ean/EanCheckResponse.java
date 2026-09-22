package ru.esm.tspiot.driver.api.model.ean;

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
    private final boolean hasBlockedItems;

    public EanCheckResponse(@NonNull List<EanCheckResult> results) {
        this.results = new ArrayList<>(results);
        boolean blocked = false;
        for (EanCheckResult result : this.results) {
            if (result.getBlocked()) {
                blocked = true;
                break;
            }
        }
        this.hasBlockedItems = blocked;
    }

    public EanCheckResponse(@NonNull List<EanCheckResult> results, boolean hasBlockedItems) {
        this.results = new ArrayList<>(results);
        this.hasBlockedItems = hasBlockedItems;
    }

    private EanCheckResponse(Parcel in) {
        results = Objects.requireNonNull(in.createTypedArrayList(EanCheckResult.CREATOR));
        hasBlockedItems = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(results);
        dest.writeByte((byte) (hasBlockedItems ? 1 : 0));
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

    public boolean getHasBlockedItems() {
        return hasBlockedItems;
    }

    @NonNull
    @Override
    public String toString() {
        return "EanCheckResponse{" +
                "results=" + results +
                ", hasBlockedItems=" + hasBlockedItems +
                '}';
    }
}
