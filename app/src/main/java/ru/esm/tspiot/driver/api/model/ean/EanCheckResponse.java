package ru.esm.tspiot.driver.api.model.ean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

@Keep
public final class EanCheckResponse implements Parcelable {
    private final List<EanCheckResult> results;
    private final boolean hasBlockedItems;
    private final GtinCheckError error;

    public EanCheckResponse(
            @Nullable List<EanCheckResult> results,
            boolean hasBlockedItems,
            @Nullable GtinCheckError error
    ) {
        this.results = results;
        this.hasBlockedItems = hasBlockedItems;
        this.error = error;
    }

    private EanCheckResponse(Parcel in) {
        results = in.createTypedArrayList(EanCheckResult.CREATOR);
        hasBlockedItems = in.readInt() == 1;
        error = in.readInt() == 1 ? in.readParcelable(GtinCheckError.class.getClassLoader()) : null;
    }

    @Nullable
    public List<EanCheckResult> getResults() {
        return results;
    }

    public boolean getHasBlockedItems() {
        return hasBlockedItems;
    }

    @Nullable
    public GtinCheckError getError() {
        return error;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(results);
        dest.writeInt(hasBlockedItems ? 1 : 0);
        if (error == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            dest.writeParcelable(error, flags);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EanCheckResponse> CREATOR = new Creator<EanCheckResponse>() {
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
    @Override
    public String toString() {
        return "EanCheckResponse{" +
                "results=" + results +
                ", hasBlockedItems=" + hasBlockedItems +
                ", error=" + error +
                '}';
    }
}
