package ru.esp.esm.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Модель списка CIS для передачи через Binder/Bundle.
 */
public class CisList implements Parcelable {
    @NonNull
    private final List<Cis> codesList;
    @Nullable
    private final Integer tz;

    public CisList(@NonNull List<Cis> codesList, @Nullable Integer tz) {
        this.codesList = codesList;
        this.tz = tz;
    }

    protected CisList(@NonNull Parcel in) {
        this.codesList = Objects.requireNonNull(in.createTypedArrayList(Cis.CREATOR));
        this.tz = in.readInt() == 1 ? in.readInt() : null;
    }

    @NonNull
    public List<Cis> getCodesList() {
        return codesList;
    }

    @Nullable
    public Integer getTz() {
        return tz;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(codesList);
        // 1 = не null, 0 = null
        dest.writeInt(tz != null ? 1 : 0);
        if (tz != null) {
            dest.writeInt(tz);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CisList> CREATOR = new Creator<CisList>() {
        @Override
        public CisList createFromParcel(@NonNull Parcel in) {
            return new CisList(in);
        }

        @Override
        public CisList[] newArray(int size) {
            return new CisList[size];
        }
    };
}
